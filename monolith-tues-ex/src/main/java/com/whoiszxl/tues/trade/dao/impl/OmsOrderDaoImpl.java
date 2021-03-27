package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.OmsOrderDao;
import com.whoiszxl.tues.trade.entity.OmsOrder;
import com.whoiszxl.tues.trade.repository.OmsOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单成交记录dao接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Repository
public class OmsOrderDaoImpl implements OmsOrderDao {

    @Autowired
    private OmsOrderRepository omsOrderRepository;

    @Override
    public OmsOrder save(OmsOrder omsOrder) {
        return omsOrderRepository.save(omsOrder);
    }

    @Override
    public List<OmsOrder> findAllByMemberIdAndTransactionIdOrderByIdDesc(Long memberId, Long transactionId) {
        return omsOrderRepository.findAllByMemberIdAndTransactionIdOrderByIdDesc(memberId, transactionId);
    }
}
