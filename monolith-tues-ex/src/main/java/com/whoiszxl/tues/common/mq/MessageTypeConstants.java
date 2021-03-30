package com.whoiszxl.tues.common.mq;

/**
 * 撮合消息类型枚举
 */
public class MessageTypeConstants {

    /** 创建委托挂单 */
    public static final String NEW_ORDER = "NEW_ORDER";

    /** 取消委托挂单 */
    public static final String CANCEL_ORDER = "CANCEL_ORDER";

    /** 启动撮合引擎 */
    public static final String START_ENGINE = "START_ENGINE";

    /** 关闭撮合引擎 */
    public static final String SHUTDOWN_ENGINE = "SHUTDOWN_ENGINE";
}