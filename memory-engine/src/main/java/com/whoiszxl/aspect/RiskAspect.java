package com.whoiszxl.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 风控切面
 */
@Aspect
@Component
@Slf4j
public class RiskAspect {

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.whoiszxl.consumer.OrderMessageConsumer(..))")
    public void risk(){}


    @Before("risk()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

    }

    @AfterReturning(pointcut = "risk()")
    public void doAfterReturning() throws Throwable {

    }
}
