package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface IOrderService {

    /**
     * 创建订单
     */
    public ServerResponse create(Integer userId,Integer shippingId);
}
