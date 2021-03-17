package com.whoiszxl.tues.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 正常的开关状态枚举
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
@Getter
@AllArgsConstructor
public enum SwitchStatusEnum {

    STATUS_OPEN("开启", 1),
    STATUS_CLOSE("关闭", 0),
    STATUS_ALL("所有", 2);
    private String statusMsg;
    private Integer statusCode;

}