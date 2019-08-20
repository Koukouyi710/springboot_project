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


        return orderService.list(null,pageNum,pageSize);
    }

    /**
     * 按订单号查询
     */
    @RequestMapping(value = "/search.do")
    public ServerResponse search(HttpSession session,Long orderNo,
                                 @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                 @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize){


        return orderService.search(null,orderNo,pageNum,pageSize);
    }

    /**
     * 订单详情
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){


        return orderService.detail(null,orderNo);
    }

    /**
     * 订单发货
     */
    @RequestMapping(value = "/send_goods.do")
    public ServerResponse send_goods(HttpSession session,Long orderNo){


        return orderService.send_goods(null,orderNo);
    }
}
