package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        int result = shippingMapper.updateByPrimaryKey(shipping);
        //step3:返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess("更新成功！");
        }
        return ServerResponse.createServerResponseByFail("更新失败！");
    }

    @Override
    public ServerResponse select(Integer shippingId) {
        //step1:参数校验
        if (shippingId==null){
            return ServerResponse.createServerResponseByFail("地址id不能为空！");
        }
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if (shipping!=null){
            return ServerResponse.createServerResponseBySucess(shipping);
        }
        return ServerResponse.createServerResponseByFail("该地址不存在！");
    }

    @Override
    public ServerResponse list(Integer userId,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectAllByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }
}
