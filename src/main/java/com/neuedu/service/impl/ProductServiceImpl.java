package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Product;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    ProductMapper productMapper;

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
}
