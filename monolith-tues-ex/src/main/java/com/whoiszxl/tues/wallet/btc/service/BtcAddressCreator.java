package com.whoiszxl.tues.wallet.btc.service;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.wallet.ethereum.core.entity.AddressResponse;
import com.whoiszxl.tues.wallet.ethereum.core.service.AddressCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;

/**
 * 比特币地址生成器
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
@Component
public class BtcAddressCreator implements AddressCreator {

    @Autowired
    private BitcoindRpcClient bitcoinClient;

    @Override
    public Result<AddressResponse> createRechargeAddress(Long memberId) {
        //通过memberId为账号创建一个bitcoin地址
        String newAddress = bitcoinClient.getNewAddress(memberId.toString());
        return Result.buildSuccess(new AddressResponse(newAddress, null, null));
    }
}
