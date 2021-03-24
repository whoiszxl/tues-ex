package com.whoiszxl.tues.member.service;

import com.whoiszxl.tues.member.entity.UmsMemberAddress;
import com.whoiszxl.tues.member.entity.dto.UmsMemberAddressDTO;

/**
 * 用户地址服务
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
public interface MemberAddressService {

    /**
     * 通过用户ID和货币ID获取数据库中用户的地址
     * @param memberId
     * @param coinId
     * @return
     */
    UmsMemberAddressDTO getMemberAddressByCoinId(Long memberId, Integer coinId);

    /**
     * 更新或新增用户地址
     * @param umsMemberAddress 用户地址实体
     */
    UmsMemberAddressDTO save(UmsMemberAddress umsMemberAddress);
}
