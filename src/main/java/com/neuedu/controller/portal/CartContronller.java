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
}
