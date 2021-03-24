package com.whoiszxl.tues.wallet.common;

import com.whoiszxl.tues.common.constants.CoinNameConstans;
import com.whoiszxl.tues.wallet.btc.service.BtcAddressCreator;
import com.whoiszxl.tues.wallet.ethereum.core.service.AddressCreator;
import com.whoiszxl.tues.wallet.ethereum.eth.service.EthAddressCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 地址创建工厂
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
@Component
public class AddressCreatorFactory {


    @Autowired
    private BtcAddressCreator btcAddressCreator;

    @Autowired
    private EthAddressCreator ethAddressCreator;

    /**
     * 通过货币名获取创建地址实例
     * @param coinName
     * @return
     */
    public AddressCreator get(String coinName) {
        switch (coinName) {
            case CoinNameConstans.BTC:
                return btcAddressCreator;
            case CoinNameConstans.ETH:
                return ethAddressCreator;
            default:
                return null;
        }
    }

}
