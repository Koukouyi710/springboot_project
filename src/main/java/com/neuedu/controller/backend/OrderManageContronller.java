package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/order")
public class OrderManageContronller {

    @Autowired
    IOrderService orderService;

    /**
     * 订单列表
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NO_PRIVILEGE.getCode(),Const.ResponseCodeEunm.NO_PRIVILEGE.getDesc());
        }

        return orderService.list(null,pageNum,pageSize);
    }

    /**
     * 按订单号查询
     */
    @RequestMapping(value = "/search.do")
    public ServerResponse search(HttpSession session,Long orderNo,
                                 @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                 @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NO_PRIVILEGE.getCode(),Const.ResponseCodeEunm.NO_PRIVILEGE.getDesc());
        }

        return orderService.search(null,orderNo,pageNum,pageSize);
    }

    /**
     * 订单详情
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NO_PRIVILEGE.getCode(),Const.ResponseCodeEunm.NO_PRIVILEGE.getDesc());
        }

        return orderService.detail(null,orderNo);
    }

    /**
     * 订单发货
     */
    @RequestMapping(value = "/send_goods.do")
    public ServerResponse send_goods(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NO_PRIVILEGE.getCode(),Const.ResponseCodeEunm.NO_PRIVILEGE.getDesc());
        }

        return orderService.send_goods(null,orderNo);
    }
}
