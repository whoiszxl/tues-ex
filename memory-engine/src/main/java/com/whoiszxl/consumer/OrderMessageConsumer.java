package com.whoiszxl.consumer;

import com.whoiszxl.constants.MessageTypeConstants;
import com.whoiszxl.entity.ExOrder;
import com.whoiszxl.entity.Result;
import com.whoiszxl.orderbook.OrderBook;
import com.whoiszxl.orderbook.OrderBookFactory;
import com.whoiszxl.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 委托订单消息消费者
 */
@Slf4j
@Component
public class OrderMessageConsumer {

    @Autowired
    private OrderBookFactory orderBookFactory;

    @KafkaListener(topics = MessageTypeConstants.NEW_ORDER, groupId = "default-group")
    public void newOrderSub(ConsumerRecord<String, String> record,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                            Consumer consumer) {
        consumer.commitSync();
        log.info("订阅到新订单，开始处理, 主题为：{}， 消息值为：{}", topic, record.value());
        ExOrder exOrder = JsonUtil.fromJson(record.value(), ExOrder.class);
        if(ObjectUtils.isEmpty(exOrder) || exOrder.getOrderId() == null) {
            return;
        }

        OrderBook orderBook = orderBookFactory.getOrderBook(exOrder.getPairName());

        if(orderBook.getPause() || !orderBook.getReady()) {
            //TODO 订单簿未初始化完成或已暂停，直接撤回订单

        }

        if(!orderBook.getPause() && orderBook.getReady()) {
            //开始执行交易逻辑
            try {
                long startTime = System.currentTimeMillis();
                Result result = orderBook.newOrder(exOrder);
                long endTime = System.currentTimeMillis();
                if(result.isOk()) {
                    consumer.commitSync();
                    log.info("完成此撮合，订单ID={}, 耗时：{}ms", exOrder.getOrderId(), endTime - startTime);
                    return;
                }
            }catch (Exception e) {
                log.error("撮合失败", e);
            }
            //TODO 撮合失败需要撤回订单
        }

    }


    @KafkaListener(topics = MessageTypeConstants.CANCEL_ORDER)
    public void cancelOrderSub(ConsumerRecord<String, String> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

    }

}
