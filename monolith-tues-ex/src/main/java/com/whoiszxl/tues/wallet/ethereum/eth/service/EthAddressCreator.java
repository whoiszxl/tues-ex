package com.whoiszxl.tues.wallet.ethereum.eth.service;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.wallet.ethereum.core.entity.EthereumAddress;
import com.whoiszxl.tues.wallet.ethereum.core.service.AddressCreator;
import com.whoiszxl.tues.wallet.ethereum.core.service.EthereumService;
import com.whoiszxl.tues.wallet.ethereum.core.entity.AddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

/**
 * 以太币服务
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
@Component
public class EthAddressCreator implements AddressCreator {

    public static final String CURRENCY_NAME = "ETH";

    @Autowired
    private EthereumService ethereumService;

    @Override
    public Result<AddressResponse> createRechargeAddress(Long memberId) {
        //创建地址
        EthereumAddress ethereumAddress = ethereumService.createAddressByFile();

        return Result.buildSuccess(new AddressResponse(
                ethereumAddress.getAddress(),
                ethereumAddress.getKeystoreName(),
                ethereumAddress.getMnemonic()
        ));
    }
}
