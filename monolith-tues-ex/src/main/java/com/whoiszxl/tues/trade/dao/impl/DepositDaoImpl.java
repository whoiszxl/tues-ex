package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.DepositDao;
import com.whoiszxl.tues.trade.entity.OmsDeposit;
import com.whoiszxl.tues.trade.repository.OmsDepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值dao接口实现
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Repository
public class DepositDaoImpl implements DepositDao {

    @Autowired
    private OmsDepositRepository omsDepositRepository;

    @Override
    public OmsDeposit findByToAddressAndCoinNameAndDepositActual(String toAddress, String coinName, BigDecimal amount) {
        return omsDepositRepository.findByToAddressAndCoinNameAndDepositActual(toAddress, coinName, amount);
    }

    @Override
    public List<OmsDeposit> findByCoinNameAndUpchainStatus(String coinName, Integer upchainStatus) {
        return omsDepositRepository.findByCoinNameAndUpchainStatus(coinName, upchainStatus);
    }

    @Override
    public OmsDeposit save(OmsDeposit omsDeposit) {
        return omsDepositRepository.save(omsDeposit);
    }
}
