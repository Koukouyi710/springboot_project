package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICartService {

    /**
     * 购物车列表
     */
    public ServerResponse list(Integer userId);

    /**
     * 添加商品
     */
    public ServerResponse add(Integer userId,Integer productId,Integer count);
}
