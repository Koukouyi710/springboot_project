package com.neuedu.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日志服务切面类
 */
@Aspect
@Component
public class LogAspect {

    //定义切入点表达式
    @Pointcut("execution(* com.neuedu.service.impl.ProductServiceImpl.*(..))")
    public void pointcut(){}

    //前置通知
    @Before("pointcut()")
    public void before(){
        System.out.println("=========开始执行=========");
    }

    //后置通知
    @After("pointcut()")
    public void after(){
        System.out.println("=========执行结束=========");
    }
}
