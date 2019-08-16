package com.neuedu.controller.portal;


import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    IProductService productService;

    /**
     * 商品详情
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,
                                 @RequestParam(name = "productId",required = false) Integer productId,
                                 @RequestParam(name = "is_new",required = false) Integer is_new ,
                                 @RequestParam(name = "is_hot",required = false) Integer is_hot ,
                                 @RequestParam(name = "is_banner",required = false) Integer is_banner ){
        return productService.udetail(productId,is_new,is_hot,is_banner);
    }

    /**
     * 搜索商品
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(name = "categoryId",required = false) Integer categoryId,
                               @RequestParam(name = "keyword",required = false) String keyword,
                               @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                               @RequestParam(name ="orderBy",required = false,defaultValue = "")String orderBy){
        return productService.ulist(categoryId,keyword,pageNum,pageSize,orderBy);
    }


    /**
     * 获取商品分类
     */
    @RequestMapping(value = "/topcategory.do")
    public ServerResponse topcategory(HttpSession session,
                               @RequestParam(name = "sid",required = false,defaultValue = "0") Integer sid){
        return productService.topcategory(sid);
    }

    /**
     * 日志调用空接口
     */
    @RequestMapping(value = "/logempty.do")
    public ServerResponse logempty(){
        return ServerResponse.createServerResponseBySucess("调用成功！");
    }
}
