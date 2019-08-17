package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IAddressService {
    /**
     * 添加地址
     */
    public ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 删除地址
     */
    public ServerResponse del(Integer userId, Integer shippingId);

    /**
     * 登录状态更新地址
     */
    public ServerResponse update(Shipping shipping);

}
