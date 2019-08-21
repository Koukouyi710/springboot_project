package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.FTPUtil;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.vo.ProductCategoryVO;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ICategoryService categoryService;

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
        productDetailVO.setIs_new(product.getIsNew());
        productDetailVO.setIs_hot(product.getIsHot());
        productDetailVO.setIs_banner(product.getIsBanner());
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
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOList);
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
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    @Override
    public ServerResponse upload(MultipartFile file, String path) {
        if (file==null){
            return ServerResponse.createServerResponseByFail("请上传图片");
        }
        //获取名称
        String orignalFileName = file.getOriginalFilename();
        //获取扩展名
        String exName = orignalFileName.substring(orignalFileName.lastIndexOf("."));
        //生成新名
        String newFileName = UUID.randomUUID().toString()+exName;

        File pathFile = new File(path);
        if (!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdir();
        }
        File file1 = new File(path,newFileName);
        try {
            file.transferTo(file1);
            //上传图片服务器
            FTPUtil.uploadFile(Lists.newArrayList(file1));
            //...
            Map<String,String> map = Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+newFileName);

            //删除应用服务器上的图片
            file1.delete();

            return ServerResponse.createServerResponseBySucess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.createServerResponseByFail("");
    }

    @Override
    public ServerResponse udetail(Integer productId, Integer is_new, Integer is_hot, Integer is_banner) {
        if (productId!=null){
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product==null){
                return ServerResponse.createServerResponseByFail("商品不存在");
            }
            if (product.getStatus()!= Const.ProductStatusEunm.PRODUCT_ONLINE.getCode()){
                return ServerResponse.createServerResponseByFail("商品未上架或已删除！");
            }
            ProductDetailVO productDetailVO = assembleProductDetailVO(product);
            return ServerResponse.createServerResponseBySucess(productDetailVO);
        }
        List<Product> productList = productMapper.selectByMark(is_new,is_hot,is_banner);
        List<ProductDetailVO> productDetailVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){
                ProductDetailVO productDetailVO = assembleProductDetailVO(product);
                productDetailVOList.add(productDetailVO);
            }
        }
        return ServerResponse.createServerResponseBySucess(productDetailVOList);
    }
    @Override
    public ServerResponse ulist(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //step1:参数校验 categoryid与keyword不能同时为空
        if (categoryId==null&&(keyword==null||keyword.equals(""))){
            return ServerResponse.createServerResponseByFail("参数错误！");
        }
        //step2:categoryId
        Set<Integer> integerSet = Sets.newHashSet();
        if (categoryId!=null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category==null&&(keyword==null||keyword.equals(""))){
                //没有商品数据
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.createServerResponseBySucess(pageInfo);
            }
            ServerResponse serverResponse = categoryService.get_deep_category(categoryId);
            if (serverResponse.isSucess()){
                integerSet = (Set<Integer>)serverResponse.getData();
            }
        }
        if (keyword!=null&&!keyword.equals("")){
            keyword="%"+keyword+"%";
        }
        if (orderBy.equals("")){
            PageHelper.startPage(pageNum,pageSize);
        }else{
            String[] orderByArr = orderBy.split("_");
            if (orderByArr.length>1){
                PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);
            }else{
                PageHelper.startPage(pageNum,pageSize);
            }
        }
        List<Product> productList = productMapper.searchProduct(integerSet,keyword);
        PageInfo pageInfo = new PageInfo(productList);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        pageInfo.setList(productListVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    @Override
    public ServerResponse topcategory(Integer sid) {
        if (sid==null||sid.equals("")){
            return ServerResponse.createServerResponseByFail("类别id不能为空！");
        }
        //step2:查询product
        List<Product> productList= productMapper.selectBySId(sid);
        if (productList==null){
            return ServerResponse.createServerResponseByFail("不存在该类别的商品");
        }
        //step3:product --> productDetailVO
        List<ProductCategoryVO> productCategoryVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){
                ProductCategoryVO productCategoryVO = assembleProductCategoryVO(product);
                productCategoryVOList.add(productCategoryVO);
            }
        }
        //step4:返回结果
        return ServerResponse.createServerResponseBySucess(productCategoryVOList);

    }

    private ProductCategoryVO assembleProductCategoryVO(Product product){
        ProductCategoryVO productCategoryVO = new ProductCategoryVO();
        productCategoryVO.setCreateTime(DateUtils.dateToString(product.getCreateTime()));
        productCategoryVO.setName(product.getName());
        productCategoryVO.setId(product.getId());
        productCategoryVO.setStatus(product.getStatus());
        productCategoryVO.setUpdateTime(DateUtils.dateToString(product.getUpdateTime()));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category!=null){
            productCategoryVO.setParentCategoryId(category.getParentId());
            productCategoryVO.setSortOrder(category.getSortOrder());
        }else{
            productCategoryVO.setParentCategoryId(0);
        }
        return productCategoryVO;
    }

    @Override
    public ServerResponse findProductById(Integer productId) {
        if (productId==null){
            return ServerResponse.createServerResponseByFail("商品id不能为空！");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByFail("商品不存在！");
        }
        return ServerResponse.createServerResponseBySucess(product);
    }
}
