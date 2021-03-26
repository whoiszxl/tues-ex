package com.whoiszxl.tues.trade.repository;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.trade.entity.OmsCoin;
import com.whoiszxl.tues.trade.entity.OmsDeposit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值记录repository服务
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
public interface OmsDepositRepository extends BaseRepository<OmsDeposit> {

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
     * 通过交易Hash和货币名称判断交易是否存在数据库中
     * @param txHash 交易Hash
     * @param coinName 币种名称
     * @return
     */
    Boolean existsByTxHashAndCoinName(String txHash, String coinName);

    /**
     * 通过用户ID分页查询充值记录
     * @param memberId 用户ID
     * @param pageable 分页参数
     * @return
     */
    Page<OmsDeposit> findAllByMemberIdOrderById(Long memberId, Pageable pageable);

}
