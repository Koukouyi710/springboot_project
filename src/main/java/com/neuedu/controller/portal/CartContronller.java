package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/cart")
public class CartContronller {

    @Autowired
    ICartService cartService;

    /**
     * 购物车列表
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        return cartService.list(userInfo.getId());
    }

    /**
     * 添加商品
     */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session,
                              Integer productId,
                              Integer count){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        return cartService.add(userInfo.getId(),productId,count);
    }

    /**
     * 更新购物车某个商品的数量
     */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session,
                              Integer productId,
                              Integer count){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        return cartService.update(userInfo.getId(),productId,count);
    }

    /**
     * 移除购物车某些商品
     */
    @RequestMapping(value = "/delete_product.do")
    public ServerResponse delete_product(HttpSession session,
                                 String productIds){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        return cartService.delete_product(userInfo.getId(),productIds);
    }
}
