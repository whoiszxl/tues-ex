package com.whoiszxl.consumer;

import com.whoiszxl.enums.MessageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static com.whoiszxl.enums.MessageTypeEnum.NEW_ORDER;

/**
 * 委托订单消息消费者
 */
@Slf4j
@Component
public class OrderMessageConsumer {

    @KafkaListener(topics = NEW_ORDER.getCode())
    public void newOrderSub(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

    }

}
