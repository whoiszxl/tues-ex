package com.whoiszxl.tues.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 买卖枚举
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Getter
@AllArgsConstructor
public enum BuySellEnum {

    //主页下的八个分类图标
    BUY("BUY", 1),
    SELL("SELL", -1);

    private String key;
    private Integer value;
}