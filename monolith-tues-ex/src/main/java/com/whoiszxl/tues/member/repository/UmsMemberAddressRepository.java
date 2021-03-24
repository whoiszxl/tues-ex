package com.whoiszxl.tues.member.repository;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.member.entity.UmsMember;
import com.whoiszxl.tues.member.entity.UmsMemberAddress;

/**
 * 用户地址repository服务
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
public interface UmsMemberAddressRepository extends BaseRepository<UmsMemberAddress> {

    /**
     * 通过用户ID和货币ID和状态获取用户地址信息
     * @param memberId 用户ID
     * @param coinId 货币ID
     * @param status 状态
     * @return 用户地址信息
     */
    UmsMemberAddress findByMemberIdAndCoinIdAndStatus(Long memberId, Integer coinId, Integer status);

    /**
     * 通过货币ID和充值地址和状态获取用户地址信息
     * @param coinId 货币ID
     * @param depositAddress 重置地址
     * @param status 状态
     * @return 用户地址信息
     */
    UmsMemberAddress findByCoinIdAndDepositAddressAndStatus(Long coinId, String depositAddress, Integer status);
}
