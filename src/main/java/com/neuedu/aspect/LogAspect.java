package com.neuedu.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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

    //抛异常通知
    @AfterThrowing("pointcut()")
    public void afterthrow(){
        System.out.println("=========发生错误=========");
    }

    //返回时通知
    @AfterReturning("pointcut()")
    public void afterreturn(){
        System.out.println("=========返回结果=========");
    }

    //环绕通知
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint){
        Object obj = null;
        try {
            System.out.println("=====Around—before=====");
            //执行切入点匹配的方法
            obj = proceedingJoinPoint.proceed();
            System.out.println("=====Around—after=====");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("=====Around—throwing=====");
        }
        System.out.println("=====Around—after_returning=====");

        return obj;
    }

}
