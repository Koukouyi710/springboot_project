package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface IOrderService {

    /**
     * 创建订单
     */
    public ServerResponse create(Integer userId,Integer shippingId);

    /**
     *获取订单的商品信息
     */
    public ServerResponse get_order_cart_product(Integer userId);

    /**
     *订单列表
     */
    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);

    /**
     *订单详情
     */
    public ServerResponse detail(Integer userId,Long orderNo);


    /**
     *取消订单
     */
    public ServerResponse cancel(Integer userId,Long orderNo);

    /**
     *按订单号搜索
     */
    public ServerResponse search(Integer userId,Long orderNo,Integer pageNum,Integer pageSize);

    /**
     *订单发货
     */
    public ServerResponse send_goods(Integer userId,Long orderNo);


}
