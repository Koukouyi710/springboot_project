package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.config.AppConfig;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    AppConfig appConfig;



    @RequestMapping("/driver")
    public String getDriver() {
        return appConfig.getDriver()+" "+appConfig.getUsername()+" "+appConfig.getPassword();
    }


    @RequestMapping("/test")
    public ServerResponse res() {
        return ServerResponse.createServerResponseBySucess(null,"hello");
    }



}
