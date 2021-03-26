package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.OmsDeposit;
import com.whoiszxl.tues.trade.entity.OmsHeight;
import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsDepositDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsHeightDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值服务接口
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
public interface DepositService {

    /**
     * 通过货币名称获取当前同步的区块高度
     * @param coinName 货币名称
     * @return 区块高度
     */
    OmsHeightDTO getCurrentHeight(String coinName);

    /**
     * 通过货币名称获取或创建当前同步的区块高度
     * @param coinName 货币名称
     * @return 区块高度
     */
    OmsHeightDTO getOrCreateCurrentHeight(String coinName, OmsCoinDTO coinInfo, Long networkBlockHeight);

    /**
     * 通过收款地址与货币名称与金额获取充值记录
     * @param toAddress 收款地址
     * @param coinName 货币名称
     * @param amount 金额
     * @return 充值记录
     */
    OmsDepositDTO getDeposit(String toAddress, String coinName, BigDecimal amount);

    /**
     * 更新充值记录
     * @param deposit 记录
     */
    OmsDepositDTO updateDeposit(OmsDeposit omsDeposit);

    /**
     * 更新或者新增充值记录
     * @param deposit 充值记录
     */
    OmsDepositDTO saveDeposit(OmsDeposit omsDeposit);


    /**
     * 更新当前区块高度
     * @param height 区块高度记录
     */
    OmsHeightDTO saveCurrentHeight(OmsHeight height);


    /**
     * 通过货币名称获取所有待确认的充值单
     * @param coinName 货币名称
     */
    List<OmsDepositDTO> getWaitConfirmDeposit(String coinName);

    /**
     * 通过交易Hash和币种名称查询交易是否在库中存在
     * @param txHash 交易Hash
     * @param coinName 币种名称
     * @return
     */
    boolean checkTxIsExist(String txHash, String coinName);
}
