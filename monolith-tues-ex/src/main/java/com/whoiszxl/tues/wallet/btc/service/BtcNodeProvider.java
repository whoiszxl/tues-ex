package com.whoiszxl.tues.wallet.btc.service;

import com.whoiszxl.tues.wallet.ethereum.core.entity.AddressResponse;
import com.whoiszxl.tues.wallet.ethereum.core.service.NodeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 比特币地址生成器
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
@Component
public class BtcNodeProvider implements NodeProvider {

    @Autowired
    private BitcoindRpcClient bitcoinClient;

    @Override
    public AddressResponse createRechargeAddress(Long memberId) {
        //通过memberId为账号创建一个bitcoin地址
        String newAddress = bitcoinClient.getNewAddress(memberId.toString());
        return new AddressResponse(newAddress, null, null);
    }

    @Override
    public BigDecimal getAllBalance() {
        return bitcoinClient.getBalance();
    }

    @Override
    public BigDecimal getBalance(String accountOrAddress) {
        return bitcoinClient.getBalance(accountOrAddress);
    }

    @Override
    public Long getBlockCount() {
        return Long.valueOf(bitcoinClient.getBlockCount());
    }

    @Override
    public Object getBlockchainInfo() {
        return bitcoinClient.getBlockChainInfo();
    }

    @Override
    public String sendTransactionWithNode(String toAddress, BigDecimal amount, String comment, String commentTo) {
        return bitcoinClient.sendToAddress(toAddress, amount, comment, commentTo);
    }

    @Override
    public String sendTransactionWithFile(String walletFile, String password, String toAddress, BigDecimal amount) {
        return null;
    }
}
