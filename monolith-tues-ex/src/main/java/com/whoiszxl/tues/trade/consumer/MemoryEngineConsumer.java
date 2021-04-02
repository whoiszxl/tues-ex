package com.whoiszxl.tues.trade.consumer;

import com.google.gson.reflect.TypeToken;
import com.whoiszxl.tues.common.mq.MessageTypeConstants;
import com.whoiszxl.tues.common.utils.JsonUtil;
import com.whoiszxl.tues.trade.entity.OmsDeal;
import com.whoiszxl.tues.trade.entity.dto.OrderMessageDTO;
import com.whoiszxl.tues.trade.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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
    public void handleOrderSuccess(ConsumerRecord<String, String> record,
                                   Consumer consumer) {
        try{
            List<OrderMessageDTO> orderList = JsonUtil.fromJsonToList(record.value(),
                    new TypeToken<List<OrderMessageDTO>>() {
                    }.getType());

            for (OrderMessageDTO orderInfo : orderList) {
                orderService.handleOrderSuccess(
                        orderInfo.getOrderId(),
                        orderInfo.getVolume(),
                        orderInfo.getTurnover());
            }
            consumer.commitSync();
        }catch(Exception e) {
            log.error("handleOrderSuccess处理订单成功撮合消息失败", e);
        }

    }

    @KafkaListener(topics = MessageTypeConstants.HANDLE_DEAL_SUCCESS, groupId = "default-group")
    public void handleDealSuccess(ConsumerRecord<String, String> record, Consumer consumer) {
        try{
            List<OmsDeal> dealList = JsonUtil.fromJsonToList(record.value(),
                    new TypeToken<List<OmsDeal>>() {
                    }.getType());

            for (OmsDeal omsDeal : dealList) {
                orderService.handleDealSuccess(omsDeal);
            }
            consumer.commitSync();
        }catch(Exception e) {
            log.error("handleOrderSuccess处理订单成功撮合消息失败", e);
        }
    }


}
