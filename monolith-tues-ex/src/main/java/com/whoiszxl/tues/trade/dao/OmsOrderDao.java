package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.trade.entity.OmsOrder;

import java.util.List;

/**
 * 订单成交记录dao接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface OmsOrderDao {

    /**
     * 保存成交记录
     * @param omsOrder
     * @return
     */
    OmsOrder save(OmsOrder omsOrder);

    /**
     * 通过用户ID和挂单ID按照主键ID降序查询成交记录
     * @param memberId 用户ID
     * @param transactionId 挂单ID
     * @return
     */
    List<OmsOrder> findAllByMemberIdAndTransactionIdOrderByIdDesc(Long memberId, Long transactionId);
}
