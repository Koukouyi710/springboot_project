package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManageController {

    @Autowired
    ICategoryService categoryService;
    /**
     * 获取品类的子节点（平级）
     */
    @RequestMapping(value = "/get_category.do")
    public ServerResponse get_category(HttpSession session,Integer categoryId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NO_PRIVILEGE.getCode(),Const.ResponseCodeEunm.NO_PRIVILEGE.getDesc());
        }

        return categoryService.get_category(categoryId);
    }

    /**
     * 增加节点
     */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(HttpSession session, Category category){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NO_PRIVILEGE.getCode(),Const.ResponseCodeEunm.NO_PRIVILEGE.getDesc());
        }

        return categoryService.add_category(category);
    }

    /**
     * 修改节点
     */
    @RequestMapping(value = "/set_category.do")
    public ServerResponse set_category(HttpSession session, Category category){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NO_PRIVILEGE.getCode(),Const.ResponseCodeEunm.NO_PRIVILEGE.getDesc());
        }

        return categoryService.set_category(category);
    }
}
