package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;

public interface IProductService {

    /**
     *新增or更新商品
     */
    public ServerResponse save(Product product);

    /**
     *商品上下架
     */
    public ServerResponse set_sale_status(Integer productId,Integer status);

    /**
     *商品详情
     */
    public ServerResponse detail(Integer productId);

    /**
     *商品列表
     */
    public ServerResponse list(Integer pageNum,Integer pageSize);

    /**
     *商品搜索
     */
    public ServerResponse search(String productName,Integer productId,Integer pageNum,Integer pageSize);

}
