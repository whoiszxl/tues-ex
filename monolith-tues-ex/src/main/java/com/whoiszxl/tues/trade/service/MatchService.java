package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.OmsOrder;

/**
 * 撮合订单服务接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface MatchService {

    /**
     * 发送消息到撮合系统中
     * @param order 挂单信息
     */
    void send(OmsOrder order, String messageType);
}
