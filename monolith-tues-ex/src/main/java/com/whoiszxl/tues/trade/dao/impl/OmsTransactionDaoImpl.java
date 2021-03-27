package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.OmsTransactionDao;
import com.whoiszxl.tues.trade.entity.OmsTransaction;
import com.whoiszxl.tues.trade.repository.OmsTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 挂单dao接口实现
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Service
public class OmsTransactionDaoImpl implements OmsTransactionDao {

    @Autowired
    private OmsTransactionRepository omsTransactionRepository;

    @Override
    public OmsTransaction save(OmsTransaction transaction) {
        return omsTransactionRepository.save(transaction);
    }

    @Override
    public List<OmsTransaction> getBuyMatchTransactionList(Integer type, Long memberId, Integer coinId, Integer replaceCoinId, BigDecimal price) {
        return omsTransactionRepository.getBuyMatchTransactionList(type, memberId, coinId, replaceCoinId, price);
    }

    @Override
    public List<OmsTransaction> getSellMatchTransactionList(Integer type, Long memberId, Integer coinId, Integer replaceCoinId, BigDecimal price) {
        return omsTransactionRepository.getSellMatchTransactionList(type, memberId, coinId, replaceCoinId, price);
    }

    @Override
    public void changeCountAndStatus(BigDecimal transactionCount, Integer status, Long id) {
        omsTransactionRepository.changeCountAndStatus(transactionCount, status, id);
    }

    @Override
    public List<OmsTransaction> findAllByMemberIdOrderByIdDesc(Long memberId) {
        return omsTransactionRepository.findAllByMemberIdOrderByIdDesc(memberId);
    }
}
