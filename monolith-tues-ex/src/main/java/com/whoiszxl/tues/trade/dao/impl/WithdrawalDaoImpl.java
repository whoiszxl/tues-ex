package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.WithdrawalDao;
import com.whoiszxl.tues.trade.entity.OmsWithdrawal;
import com.whoiszxl.tues.trade.repository.OmsWithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * 提现记录dao实现
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Repository
public class WithdrawalDaoImpl implements WithdrawalDao {

    @Autowired
    private OmsWithdrawalRepository omsWithdrawalRepository;

    @Override
    public Page<OmsWithdrawal> findAllByMemberIdOrderById(Long memberId, Pageable pageable) {
        return omsWithdrawalRepository.findAllByMemberIdOrderById(memberId, pageable);
    }
}
