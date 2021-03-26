package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.trade.entity.OmsDeposit;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值dao接口
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
public interface DepositDao {

    /**
     * 通过收款地址与货币名称与金额获取OmsDeposit记录
     * @param toAddress 收款地址
     * @param coinName 货币名称
     * @param amount 金额
     * @return 充值记录
     */
    OmsDeposit findByToAddressAndCoinNameAndDepositActual(String toAddress, String coinName, BigDecimal amount);

    /**
     * 通过货币名称和上链状态获取充值单列表
     * @param coinName 货币名称
     * @param upchainStatus 上链状态
     * @return
     */
    List<OmsDeposit> findByCoinNameAndUpchainStatus(String coinName, Integer upchainStatus);

    /**
     * 更新或新增充值记录
     * @param omsDeposit 充值记录
     * @return
     */
    OmsDeposit save(OmsDeposit omsDeposit);

    /**
     * 通过交易Hash和货币名称判断交易是否存在数据库中
     * @param txHash 交易Hash
     * @param coinName 币种名称
     * @return
     */
    Boolean existsByTxHashAndCoinName(String txHash, String coinName);
}
