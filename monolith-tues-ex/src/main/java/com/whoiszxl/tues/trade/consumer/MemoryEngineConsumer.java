package com.whoiszxl.tues.trade.consumer;

import com.whoiszxl.tues.common.mq.MessageTypeConstants;
import com.whoiszxl.tues.common.utils.JsonUtil;
import com.whoiszxl.tues.trade.entity.dto.OrderMessageDTO;
import com.whoiszxl.tues.trade.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 撮合信息处理消费者
 *
 * @author zhouxiaolong
 * @date 2021/4/1
 */
@Slf4j
@Component
public class MemoryEngineConsumer {

    @Autowired
    private OrderService orderService;


    @KafkaListener(topics = MessageTypeConstants.HANDLE_ORDER_SUCCESS, groupId = "default-group")
    public void handleOrderSuccess(List<ConsumerRecord<String, String>> records,
                                   Acknowledgment ack,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                   Consumer consumer) {
        try{
            for (ConsumerRecord<String, String> record : records) {
                OrderMessageDTO orderInfo = JsonUtil.fromJson(record.value(), OrderMessageDTO.class);
                orderService.handleOrderSuccess(orderInfo.getOrderId(), orderInfo.getVolume(), orderInfo.getTurnover());
            }

        }catch(Exception e) {
            log.error("handleOrderSuccess处理订单成功撮合消息失败", e);
        }

    }


    @KafkaListener(topics = MessageTypeConstants.HANDLE_DEAL_SUCCESS, groupId = "default-group")
    public void handleDealSuccess(List<ConsumerRecord<String, String>> record,
                                  Acknowledgment ack,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  Consumer consumer) {

    }


}