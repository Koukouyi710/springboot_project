package com.neuedu.controller.portal;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IOrderService;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderContronller {

    @Autowired
    IOrderService orderService;

    /**
     *创建订单
     */
    @RequestMapping(value = "/create.do")
    public ServerResponse create(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        return orderService.create(userInfo.getId(),shippingId);
    }
    /**
     *获取购物车的订单明细
     */
    @RequestMapping(value = "/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        return orderService.get_order_cart_product(userInfo.getId());
    }

    /**
     *订单列表
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize
    ){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        return orderService.list(userInfo.getId(),pageNum,pageSize);
    }

    /**
     *订单详情
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        return orderService.detail(userInfo.getId(),orderNo);
    }

    /**
     *取消订单
     */
    @RequestMapping(value = "/cancel.do")
    public ServerResponse cancel(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        return orderService.cancel(userInfo.getId(),orderNo);
    }

    /**
     *支付
     */
    @RequestMapping(value = "/pay.do")
    public ServerResponse pay(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        return orderService.pay(userInfo.getId(),orderNo);
    }

    /**
     *查询订单支付状态
     */
    @RequestMapping(value = "/query_order_pay_status.do")
    public ServerResponse query_order_pay_status(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        return orderService.query_order_pay_status(userInfo.getId(),orderNo);
    }

    /**
     * 支付宝服务器回调接口
     */
    @RequestMapping(value = "/alipay_callback.do")
    public ServerResponse alipay_callback(HttpSession session, HttpServletRequest request){

        System.out.println("====支付宝回调====");

        Map<String,String[]> params = request.getParameterMap();
        Map<String,String> requestparams = Maps.newHashMap();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            String[] strArr = params.get(key);
            String value = "";
            for (int i =0;i<strArr.length;i++){
                value = (i==strArr.length-1)?value+strArr[i]:value+strArr[i]+",";
            }
            requestparams.put(key,value);
        }

        //支付宝验证签名
        try {
            requestparams.remove("sign_type");
            boolean result = AlipaySignature.rsaCheckV2(requestparams, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if (!result){
                return ServerResponse.createServerResponseByFail("非法请求，验证不通过！");
            }
            //处理业务逻辑
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //return ServerResponse.createServerResponseBySucess("回调成功！");
        return orderService.alipay_callback(requestparams);
    }

}
