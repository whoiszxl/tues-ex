package com.whoiszxl.tues.wallet.ethereum.core.service;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.wallet.ethereum.core.entity.AddressResponse;

/**
 * 地址创建接口
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
public interface AddressCreator {

    /**
     * 创建一个货币的地址
     * @return
     */
    Result<AddressResponse> createRechargeAddress(Long memberId);
}
