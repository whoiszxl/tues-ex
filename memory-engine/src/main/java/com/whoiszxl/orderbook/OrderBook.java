package com.whoiszxl.orderbook;

import com.whoiszxl.entity.ExOrder;
import com.whoiszxl.entity.Result;

/**
 * 委托单簿服务接口
 */
public interface OrderBook {

    /**
     * 获取是否暂停的状态
     * @return
     */
    boolean getPause();

    /**
     * 获取是否准备就绪的状态
     * @return
     */
    boolean getReady();

    /**
     * 创建订单
     * @param exOrder 订单信息
     * @return
     */
    Result newOrder(ExOrder exOrder);

    /**
     * 撤销订单
     * @param orderId 订单ID
     * @return
     */
    Result cancelOrder(Long orderId);
}
