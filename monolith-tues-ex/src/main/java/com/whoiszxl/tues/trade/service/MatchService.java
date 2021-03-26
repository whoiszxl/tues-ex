package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.OmsTransaction;

/**
 * 撮合订单服务接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface MatchService {

    /**
     * 异步调用撮合的接口功能
     * @param transaction 挂单信息
     */
    void matchOrder(OmsTransaction transaction);
}
