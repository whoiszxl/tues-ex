package com.whoiszxl.tues.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 挂单状态枚举
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Getter
@AllArgsConstructor
public enum TransactionStatusEnum {

    TRADE_OPEN("代表部分交易，可交易", 2),
    TRADE_CLOSE("所有已成交，交易结束", 1),
    TRADE_CANCEL("用户撤单", -1)
    ;

    private String message;
    private Integer value;

}