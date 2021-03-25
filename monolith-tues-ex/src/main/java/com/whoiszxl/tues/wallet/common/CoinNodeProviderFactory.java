package com.whoiszxl.tues.wallet.common;

import com.whoiszxl.tues.common.constants.CoinNameConstants;
import com.whoiszxl.tues.wallet.btc.service.BtcNodeProvider;
import com.whoiszxl.tues.wallet.ethereum.core.service.NodeProvider;
import com.whoiszxl.tues.wallet.ethereum.eth.service.EthNodeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 地址创建工厂
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
@Component
public class CoinNodeProviderFactory {

    @Autowired
    private BtcNodeProvider btcNodeProvider;

    @Autowired
    private EthNodeProvider ethNodeProvider;

    /**
     * 通过货币名获取创建地址实例
     * @param coinName
     * @return
     */
    public NodeProvider get(String coinName) {
        switch (coinName) {
            case CoinNameConstants.BTC:
                return btcNodeProvider;
            case CoinNameConstants.ETH:
                return ethNodeProvider;
            default:
                return null;
        }
    }

}
