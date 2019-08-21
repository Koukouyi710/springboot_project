package com.neuedu.interceptors;

import com.alibaba.druid.support.json.JSONUtils;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.utils.JsonUtils;
import org.apache.catalina.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 拦截后台请求，验证用户是否登录
 */
@Component
public class AdminAuthroityInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnsupportedEncodingException {

        System.out.println("===preHandle===");

        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENT_USER);
        if (userInfo==null){
            //return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
            response.reset();;
            try {
                response.setHeader("Content-Type","application/json;charset=UTF-8");
                PrintWriter printWriter = response.getWriter();
                ServerResponse serverResponse = ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NEED_LOGIN.getCode(),Const.ResponseCodeEunm.NEED_LOGIN.getDesc());
                String json = JsonUtils.obj2String(serverResponse);
                //String json = "需要登录";
                printWriter.write(json);
                printWriter.flush();
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            //return ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NO_PRIVILEGE.getCode(),Const.ResponseCodeEunm.NO_PRIVILEGE.getDesc());
            response.reset();;
            try {
                response.setHeader("Content-Type","application/json;charset=UTF-8");
                PrintWriter printWriter = response.getWriter();
                ServerResponse serverResponse = ServerResponse.createServerResponseByFail(Const.ResponseCodeEunm.NO_PRIVILEGE.getCode(),Const.ResponseCodeEunm.NO_PRIVILEGE.getDesc());
                String json = JsonUtils.obj2String(serverResponse);
                //String json = "没有权限";
                printWriter.write(json);
                printWriter.flush();
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        System.out.println("===postHandle===");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("===afterCompletion===");
    }
}
