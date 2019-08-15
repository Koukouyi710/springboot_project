package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;

public interface IProductService {

    /**
     *新增or更新商品
     */
    public ServerResponse save(Product product);
}
