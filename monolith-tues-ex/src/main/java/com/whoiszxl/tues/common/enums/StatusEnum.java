package com.whoiszxl.tues.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态类型
 * @author zhouxiaolong
 * @date 2021/3/17
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    CLOSE(0, "关闭"),
    OPEN(1, "开启"),
    ;
    private Integer code;
    private String desc;
}