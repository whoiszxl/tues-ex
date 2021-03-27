package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.dto.OmsOrderDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsTransactionDTO;
import com.whoiszxl.tues.trade.entity.param.TransactionParam;

import java.util.List;

/**
 * 挂单服务
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface TransactionService {

    /**
     * 挂单
     * @param transactionParam 挂单参数
     * @return
     */
    boolean add(TransactionParam transactionParam);

    /**
     * 查看指定用户的当前挂单列表
     * @param memberId 用户ID
     * @return
     */
    List<OmsTransactionDTO> listTransaction(Long memberId);

    /**
     * 查看已成交详情列表
     * @param memberId 用户ID
     * @param transactionId 挂单ID
     * @return
     */
    List<OmsOrderDTO> listOrder(Long memberId, Long transactionId);
}
