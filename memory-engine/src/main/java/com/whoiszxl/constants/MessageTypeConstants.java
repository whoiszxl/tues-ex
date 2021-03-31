package com.whoiszxl.constants;

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

    /** 订单撮合成功处理 */
    public static final String HANDLE_ORDER_SUCCESS = "HANDLE_ORDER_SUCCESS";

    /** 撮合成交详情成功处理 */
    public static final String HANDLE_DEAL_SUCCESS = "HANDLE_DEAL_SUCCESS";
}