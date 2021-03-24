package com.whoiszxl.tues.common.enums.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * 用户Redis前缀
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
@Getter
@AllArgsConstructor
public enum MemberRedisPrefixEnum {

    USER_REGISTER_PHONE_CODE("member:register:", 5, TimeUnit.MINUTES)
    ;
    private String key;
    private long time;
    private TimeUnit unit;
}
