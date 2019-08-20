package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import com.neuedu.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    IProductService productService;
    /**
     * 新增or更新商品
     */
    @RequestMapping(value = "/save.do")
    public ServerResponse save(HttpSession session, Product product){

        return productService.save(product);
    }

    /**
     * 产品上下架
     */
    @RequestMapping(value = "/set_sale_status.do")
    public ServerResponse set_sale_status(HttpSession session,Integer productId,Integer status){

        return productService.set_sale_status(productId,status);
    }

    /**
     * 查看商品详情
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Integer productId){

        return productService.detail(productId);
    }

    /**
     * 商品列表
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize){


        return productService.list(pageNum,pageSize);
    }

    /**
     * 商品搜索
     */
    @RequestMapping(value = "/search.do")
    public ServerResponse search(HttpSession session,
                                 @RequestParam(name = "productName",required = false)String productName,
                                 @RequestParam(name = "productId",required = false)Integer productId,
                                 @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                 @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize){


        return productService.search(productName,productId,pageNum,pageSize);
    }


}
