package com.whoiszxl.tues.wallet.service;

import com.whoiszxl.tues.member.entity.dto.UmsMemberAddressDTO;

/**
 * 地址分配服务
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
public interface AddressDispenseService {

    /**
     * 创建或者获取一个地址，通过用户ID和货币ID去获取
     * @param coinId 货币ID
     * @param memberId 用户ID
     * @return
     */
    UmsMemberAddressDTO createOrGetAddress(Long memberId, Integer coinId);
}
