package com.whoiszxl.orderbook;

import com.whoiszxl.entity.ExOrder;
import com.whoiszxl.entity.Result;
import org.springframework.kafka.core.KafkaTemplate;

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
     * 设置是否准备就绪
     * @return
     */
    void setReady(boolean flag);

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

    /**
     * 添加消息发送工具
     * @param kafkaTemplate
     */
    void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate);

    /**
     * 设置交易对第一个币种的精度
     * @param decimals 精度
     */
    void setFirstCoinDecimals(Integer decimals);

    /**
     * 设置交易对第二个币种的精度
     * @param decimals 精度
     */
    void setSecondCoinDecimals(Integer decimals);
}
