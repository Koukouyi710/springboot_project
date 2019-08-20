package com.neuedu;

import com.google.common.collect.Lists;
import com.neuedu.interceptors.AdminAuthroityInterceptor;
import com.neuedu.interceptors.PortalAuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
public class MySpringBootConfig implements WebMvcConfigurer{

    //拦截后台请求，验证用户是否登录
    @Autowired
    AdminAuthroityInterceptor adminAuthroityInterceptor;

    //拦截前台需要登录用户的请求
    @Autowired
    PortalAuthorityInterceptor portalAuthorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(adminAuthroityInterceptor)
                .addPathPatterns("/manage/**")
                .excludePathPatterns("/manage/user/login.do");
        //前台拦截
        List<String> addPatterns = Lists.newArrayList();
        addPatterns.add("/user/**");
        addPatterns.add("/cart/**");
        addPatterns.add("/shipping/**");
        addPatterns.add("/order/**");

        List<String> excludePathPaterns = Lists.newArrayList();
        excludePathPaterns.add("/user/login.do");
        excludePathPaterns.add("/user/register.do");
        excludePathPaterns.add("/user/forget_get_question.do");
        excludePathPaterns.add("/user/forget_check_answer.do");
        excludePathPaterns.add("/user/forget_reset_password.do");
        excludePathPaterns.add("/user/check_valid.do");
        excludePathPaterns.add("/order/alipay_callback.do");

        registry.addInterceptor(portalAuthorityInterceptor)
                .addPathPatterns(addPatterns)
                .excludePathPatterns(excludePathPaterns);
    }
}
