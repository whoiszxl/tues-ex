package com.whoiszxl.tues.wallet.ethereum.eth.service;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.wallet.ethereum.core.entity.AddressResponse;
import com.whoiszxl.tues.wallet.ethereum.core.entity.EthereumAddress;
import com.whoiszxl.tues.wallet.ethereum.core.service.EthereumService;
import com.whoiszxl.tues.wallet.ethereum.core.service.NodeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 以太币服务
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
@Component
public class EthNodeProvider implements NodeProvider {

    @Autowired
    private EthereumService ethereumService;

    @Override
    public AddressResponse createDepositAddress(Long memberId) {
        //创建地址
        EthereumAddress ethereumAddress = ethereumService.createAddressByFile();
        return new AddressResponse(
                ethereumAddress.getAddress(),
                ethereumAddress.getKeystoreName(),
                ethereumAddress.getMnemonic()
        );
    }

    @Override
    public BigDecimal getAllBalance() {
        //TODO 暂不实现
        return null;
    }

    @Override
    public BigDecimal getBalance(String accountOrAddress) {
        return ethereumService.getEthBalance(accountOrAddress);
    }

    @Override
    public Long getBlockCount() {
        return ethereumService.getBlockchainHeight();
    }

    @Override
    public Object getBlockchainInfo() {
        return null;
    }

    @Override
    public String sendTransactionWithNode(String toAddress, BigDecimal amount, String comment, String commentTo) {
        return null;
    }

    @Override
    public String sendTransactionWithFile(String walletFile, String password, String toAddress, BigDecimal amount) {
        Result<String> result = ethereumService.transfer(walletFile, password, toAddress, amount);
        return result.isOk() ? result.getData() : null;
    }
}
