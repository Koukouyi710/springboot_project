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

       /**
     * 更新购物车某个商品的数量
     */
    public ServerResponse update(Integer userId,Integer productId,Integer count);

    /**
     * 删除某些商品
     */
    public ServerResponse delete_product(Integer userId,String productIds);

    /**
     * 购物车选中/取消选中某个商品
     */
    public ServerResponse select(Integer userId,Integer productId,Integer check);

    /**
     * 购物车全选/取消全选某个商品
     */
    public ServerResponse select_all(Integer userId,Integer check);

    /**
     * 查询在购物车里的产品数量
     */
    public ServerResponse get_cart_product_count(Integer userId);

}
