package com.whoiszxl.tues.common.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MQ 主题枚举
 */
@Getter
@AllArgsConstructor
public enum MqTopicEnum {

    ADD_ORDER_TOPIC("add-order-topic", "发送一笔挂单主题");

    private final String topic;
    private final String desc;
}