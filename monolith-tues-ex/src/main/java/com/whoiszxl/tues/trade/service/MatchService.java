package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.common.mq.MessageTypeEnum;
import com.whoiszxl.tues.trade.entity.OmsTransaction;

/**
 * 撮合订单服务接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface MatchService {

    /**
     * 发送消息到撮合系统中
     * @param transaction 挂单信息
     */
    void send(OmsTransaction transaction, MessageTypeEnum messageTypeEnum);
}
