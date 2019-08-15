package com.neuedu.exception;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class MyExceptionHandlerResolver implements HandlerExceptionResolver {


    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {
        System.out.println("============="+ex.getMessage());
        ModelAndView modelAndView=null;
        if (ex instanceof MyException){
            MyException myException=(MyException) ex;

            modelAndView=new ModelAndView();
            modelAndView.setViewName("common/error");  //逻辑视图
            Map<String,Object> model= modelAndView.getModel();
            model.put("msg",ex.getMessage());
            model.put("url",((MyException) ex).getDirector());
            return modelAndView;
        }
        return resolveException(request,response,handler,ex);
    }
}
