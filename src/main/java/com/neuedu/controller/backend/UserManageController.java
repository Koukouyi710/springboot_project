package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *后台用户控制类
 */
@RestController
@RequestMapping(value = "/manage/user")
public class UserManageController {

    @Autowired
    IUserService userService;
    /**
     * 管理员登录
     */
    @RequestMapping(value = "/login.do")
    public ServerResponse login(UserInfo userInfo, HttpSession session, HttpServletResponse response){

        String pwd = MD5Utils.getMD5Code(userInfo.getPassword());
        userInfo.setPassword(pwd);
        ServerResponse serverResponse = userService.login(userInfo);
        if(serverResponse.isSucess()){
            UserInfo loginUserInfo = (UserInfo) serverResponse.getData();
            if (loginUserInfo.getRole()==Const.RoleEnum.ROLE_CUSTOMER.getCode()){
                return ServerResponse.createServerResponseByFail("无权限登录！");
            }
            session.setAttribute(Const.CURRENT_USER,loginUserInfo);
            Cookie username_cookie = new Cookie("username",loginUserInfo.getUsername());
            Cookie password_cookie = new Cookie("password",loginUserInfo.getPassword());
            username_cookie.setMaxAge(60*60*24*7);
            password_cookie.setMaxAge(60*60*24*7);

            response.addCookie(username_cookie);
            response.addCookie(password_cookie);
        }
        return serverResponse;
    }

    /**
     * 用户列表
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        return userService.list(pageSize, pageNum);
    }
}
