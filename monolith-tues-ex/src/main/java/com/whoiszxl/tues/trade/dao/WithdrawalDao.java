package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.trade.entity.OmsWithdrawal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 提现记录dao接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface WithdrawalDao {

    /**
     * 通过用户ID分页查询提现记录
     * @param memberId 用户ID
     * @param pageable 分页参数
     * @return
     */
    Page<OmsWithdrawal> findAllByMemberIdOrderById(Long memberId, Pageable pageable);
}
