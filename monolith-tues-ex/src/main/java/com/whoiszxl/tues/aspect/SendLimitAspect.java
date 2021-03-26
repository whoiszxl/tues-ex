package com.whoiszxl.tues.aspect;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.enums.redis.MemberRedisPrefixEnum;
import com.whoiszxl.tues.common.exception.ExceptionCatcher;
import com.whoiszxl.tues.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 发送限制切面
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Aspect
@Component
@Slf4j
public class SendLimitAspect {

    private static final String LIMIT_VALUE = "1";

    @Autowired
    private RedisUtils redisUtils;

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.whoiszxl.tues.member.controller.MemberController.sendVerifySms(..))")
    public void sendLimit(){}


    @Before("sendLimit()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());

        //从request中获取sessionId，并拼接获取redis key
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String key = MemberRedisPrefixEnum.LIMIT_SEND.getKey() + request.getSession().getId();

        if(StringUtils.equals(redisUtils.get(key), LIMIT_VALUE)) {
            ExceptionCatcher.catchValidateEx(Result.buildError("一分钟只允许发送一次"));
        }
    }

    @AfterReturning(pointcut = "sendLimit()")
    public void doAfterReturning() throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String key = MemberRedisPrefixEnum.LIMIT_SEND.getKey() + request.getSession().getId();
        redisUtils.setEx(key, LIMIT_VALUE, MemberRedisPrefixEnum.LIMIT_SEND.getTime(), MemberRedisPrefixEnum.LIMIT_SEND.getUnit());
        log.info("处理耗时：" + (System.currentTimeMillis() - startTime.get()) + "ms");
        startTime.remove();
    }
}
