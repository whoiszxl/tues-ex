package com.whoiszxl.tues.common.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MqEnum {

    KAFKA("KAFKA", "kafka消息队列"),
    ROCKETMQ("ROCKETMQ", "rocket mq消息队列");

    private final String name;
    private final String desc;
}