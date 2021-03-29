package com.whoiszxl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 撮合消息类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    NEW_ORDER(1, "创建委托挂单"),
    CANCEL_ORDER(2, "取消委托挂单"),

    START_ENGINE(3, "启动撮合引擎"),
    SHUTDOWN_ENGINE(4, "关闭撮合引擎"),
    ;

    public static Integer code;
    public static String desc;
}