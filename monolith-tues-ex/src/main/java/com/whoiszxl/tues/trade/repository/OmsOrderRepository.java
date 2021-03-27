package com.whoiszxl.tues.trade.repository;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.trade.entity.OmsOrder;

import java.util.List;

/**
 * 挂单成交repository服务
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface OmsOrderRepository extends BaseRepository<OmsOrder> {

    /**
     * 通过用户ID和挂单ID按照主键ID降序查询成交记录
     * @param memberId 用户ID
     * @param transactionId 挂单ID
     * @return
     */
    List<OmsOrder> findAllByMemberIdAndTransactionIdOrderByIdDesc(Long memberId, Long transactionId);

}
