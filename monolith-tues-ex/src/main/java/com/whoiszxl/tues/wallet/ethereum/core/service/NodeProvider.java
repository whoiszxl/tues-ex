package com.whoiszxl.tues.wallet.ethereum.core.service;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.wallet.ethereum.core.entity.AddressResponse;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 地址创建接口
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
public interface NodeProvider {

    /**
     * 创建一个货币的地址
     * @return 地址信息
     */
    AddressResponse createRechargeAddress(Long memberId);

    /**
     * 获取节点总余额
     * @return 总余额
     */
    BigDecimal getAllBalance();

    /**
     * 通过账号或地址获取其余额
     * @return 余额
     */
    BigDecimal getBalance(String accountOrAddress);

    /**
     * 获取当前区块数量
     * @return 当前区块数量
     */
    Long getBlockCount();

    /**
     * 获取区块链信息
     * @return
     */
    Object getBlockchainInfo();

    /**
     * 通过节点发送交易
     * @param toAddress 接收地址
     * @param amount 发送数量
     * @param comment 备注文本
     * @param commentTo 备注接收人
     * @return 交易Hash
     */
    String sendTransactionWithNode(String toAddress, BigDecimal amount, String comment, String commentTo);

    /**
     * 通过文件发送交易
     * @param walletFile 钱包文件路径
     * @param toAddress 接收地址
     * @param amount 发送数量
     * @return 交易Hash
     */
    String sendTransactionWithFile(String walletFile, String password, String toAddress, BigDecimal amount);
}
