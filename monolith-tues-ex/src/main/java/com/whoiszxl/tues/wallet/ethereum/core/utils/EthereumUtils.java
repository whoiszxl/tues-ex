package com.whoiszxl.tues.wallet.ethereum.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.web3j.utils.Numeric;

/**
 * 以太坊工具类
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
public class EthereumUtils {

    /**
     * 校验以太坊地址是否有效
     * @param address 以太坊地址
     * @return
     */
    public static boolean isVaildAddress(String address) {
        if(StringUtils.isEmpty(address) || !address.startsWith("0x")) {
            return false;
        }
        address = Numeric.cleanHexPrefix(address);
        try {
            Numeric.toBigIntNoPrefix(address);
        }catch (NumberFormatException e) {
            return false;
        }
        return address.length() == 40;
    }
}
