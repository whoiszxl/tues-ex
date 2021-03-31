package com.whoiszxl.tues.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    //主页下的八个分类图标
    LIMIT("LIMIT", 1),
    MARKET("MARKET", 2);

    private String key;
    private Integer value;
}