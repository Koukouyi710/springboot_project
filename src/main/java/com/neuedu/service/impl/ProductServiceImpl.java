package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse save(Product product) {
        //step1:参数非空校验
        if (product.getId()==null||product.getId().equals("")){
            if (product.getCategoryId()==null||product.getCategoryId().equals("")) {
                return ServerResponse.createServerResponseByFail("参数为空！");
            }
        }
        //step2:设置商品主图sub_images --> 1.jpg,2.jpg,3.jpg
        String subImages = product.getSubImages();
        if (subImages!=null&&!subImages.equals("")){
            String[] subImageArr = subImages.split(",");
            if (subImageArr.length>0){
                //设置商品主图
                product.setMainImage(subImageArr[0]);
            }
        }
        //step3:商品添加/更新
        if (product.getId()==null||product.getId().equals("")){
            //添加
            int result = productMapper.insert(product);
            if (result>0){
                return ServerResponse.createServerResponseBySucess("添加成功！");
            }else{
                return ServerResponse.createServerResponseByFail("添加失败！");
            }
        }else{
            //更新
            int result = productMapper.updateByPrimaryKey(product);
            if (result>0){
                return ServerResponse.createServerResponseBySucess("修改成功！");
            }else{
                return ServerResponse.createServerResponseByFail("修改失败！");
            }
        }
        //step4:返回结果
    }

    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
        //step1:参数非空校验
        if (productId==null||productId.equals("")){
            return ServerResponse.createServerResponseByFail("商品id不能为空！");
        }
        if (status==null||status.equals("")){
            return ServerResponse.createServerResponseByFail("商品状态不能为空！");
        }
        //step2:更新
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateByPrimaryKey(product);
        //step3:返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess();
        }
        return ServerResponse.createServerResponseByFail("修改失败");
    }

    @Override
    public ServerResponse detail(Integer productId) {
        //step1:非空校验
        if (productId==null||productId.equals("")){
            return ServerResponse.createServerResponseByFail("商品id不能为空！");
        }
        //step2:查询product
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByFail("商品不存在");
        }
        //step3:product --> productDetailVO
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        //step4:返回结果
        return ServerResponse.createServerResponseBySucess(productDetailVO);
    }

    private ProductDetailVO assembleProductDetailVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToString(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setUpdateTime(DateUtils.dateToString(product.getUpdateTime()));
        productDetailVO.setSubtitle(product.getSubtitle());
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }else{
            productDetailVO.setParentCategoryId(0);
        }
        return productDetailVO;
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList=productMapper.selectAll();
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setName(product.getName());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setStatus(product.getStatus());
        productListVO.setPrice(product.getPrice());
        return productListVO;
    }

    @Override
    public ServerResponse search(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if (productName!=null&&!productName.equals("")){
            productName="%"+productName+"%";
        }
        List<Product> productList=productMapper.findProductByProductIdAndProductName(productId,productName);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }
}
