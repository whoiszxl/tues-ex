package com.whoiszxl.orderbook;

import com.whoiszxl.entity.BucketMatchResult;
import com.whoiszxl.entity.ExOrder;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * 订单桶，
 *
 * @author zhouxiaolong
 * @date 2021/3/30
 */
public interface OrderBucket extends Comparable<OrderBucket> {

    /**
     * 新增订单到订单桶中
     * @param exOrder 订单信息
     */
    void addOrder(ExOrder exOrder);

    /**
     * 从订单桶中删除订单并返回此笔订单
     * @param orderId 订单ID
     * @return
     */
    ExOrder removeOrder(Long orderId);

    /**
     * 获取当前订单桶的价格
     * @return
     */
    BigDecimal getPrice();

    /**
     * 设置当前订单桶的价格
     * @param price
     */
    void setPrice(BigDecimal price);

    /**
     * 获取当前订单桶的交易总量
     * @return
     */
    BigDecimal getTotalCount();


    /**
     * 默认排序规则
     * 通过订单桶的价格排序
     * @return
     */
    @Override
    default int compareTo(OrderBucket o) {
        return this.getPrice().compareTo(o.getPrice());
    }

    /**
     * 匹配订单
     * @param matchCount 匹配撮合的量
     * @param exOrder 交易信息
     * @param removeOrderCallback 移除订单缓存的回调事件
     * @return
     */
    BucketMatchResult match(BigDecimal matchCount, ExOrder exOrder, Consumer<ExOrder> removeOrderCallback);

}
