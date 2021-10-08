package com.jerry.savior_web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 22454
 */
@Slf4j
@Aspect
@Order(0)
@Component
public class LogAspect {
    private final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();

    @Pointcut("execution(* com.jerry.*.controller..*.*(..))")
    public void log() {

    }

    @Around("log()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.nanoTime();

        Object proceed = proceedingJoinPoint.proceed();
        long endTime = System.nanoTime();
        log.info("服务调用时间：{} ns",endTime-startTime);
        return proceed;
    }
}
