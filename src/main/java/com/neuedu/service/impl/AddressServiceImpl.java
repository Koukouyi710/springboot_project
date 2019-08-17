package com.neuedu.service.impl;

import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService{

    @Autowired
    ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        //step1:参数校验
        if (shipping==null){
            return ServerResponse.createServerResponseByFail("参数错误！");
        }
        //step2:添加
        shipping.setUserId(userId);
        int result = shippingMapper.insert(shipping);
        if (result<=0){
            return ServerResponse.createServerResponseByFail("添加失败！");
        }
        //step3:返回结果
        Map<String,Integer> map = Maps.newHashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.createServerResponseBySucess(map);
    }

    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        //step1:参数校验
        if (shippingId==null){
            return ServerResponse.createServerResponseByFail("地址id不能为空！");
        }
        //step2:删除
        int result = shippingMapper.deleteByUserIdAndShppingId(userId,shippingId);
        //step3:返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess("删除成功！");
        }
        return ServerResponse.createServerResponseByFail("删除失败！");
    }

    @Override
    public ServerResponse update(Shipping shipping) {
        //step1:参数校验
        if (shipping==null){
            return ServerResponse.createServerResponseByFail("参数错误！");
        }
        //step2:更新

        //step3:返回结果
        
        return ServerResponse.createServerResponseByFail("删除失败！");
    }
}
