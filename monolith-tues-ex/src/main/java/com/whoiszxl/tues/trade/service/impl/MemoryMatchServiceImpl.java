package com.whoiszxl.tues.trade.service.impl;

import cn.hutool.json.JSONUtil;
import com.whoiszxl.tues.common.mq.*;
import com.whoiszxl.tues.trade.entity.OmsTransaction;
import com.whoiszxl.tues.trade.entity.dto.TransactionMessageDTO;
import com.whoiszxl.tues.trade.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 内存撮合服务实现
 * 直接将挂单信息通过MQ发送出去异步处理
 */
@Service
public class MemoryMatchServiceImpl implements MatchService {

    @Autowired
    private MqSenderFactory mqSenderFactory;
    private MqSender mqSender = mqSenderFactory.get(MqEnum.KAFKA);

    @Override
    public void send(OmsTransaction transaction, MessageTypeEnum messageTypeEnum) {
        //内存撮合需要的参数 用户ID, 交易对币种，交易方向，价格，量，挂单委托类型，挂单主键ID
        TransactionMessageDTO messageDTO = transaction.clone(TransactionMessageDTO.class);
        messageDTO.setMessageType(messageTypeEnum.getCode());
        mqSender.send(MqTopicEnum.ADD_TRANSACTION_TOPIC.getTopic(), JSONUtil.toJsonStr(messageDTO));
    }
}
