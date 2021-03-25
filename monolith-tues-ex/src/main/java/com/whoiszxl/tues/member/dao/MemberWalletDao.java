package com.whoiszxl.tues.member.dao;

import com.whoiszxl.tues.member.entity.UmsMemberWallet;

import java.math.BigDecimal;

/**
 * 用户钱包dao服务
 */
public interface MemberWalletDao {

    /**
     * 减可用余额 加锁定余额
     * @param memberId 用户ID
     * @param coinId 币种ID
     * @param amount 金额
     * @return
     */
    int lockBalance(Long memberId, Integer coinId, BigDecimal amount);

    /**
     * 加可用余额 减锁定余额
     * @param memberId 用户ID
     * @param coinId 币种ID
     * @param amount 金额
     * @return
     */
    int addBalance(Long memberId, Integer coinId, BigDecimal amount);

    /**
     * 加锁定余额
     * @param memberId 用户ID
     * @param coinId 币种ID
     * @param amount 金额
     * @return
     */
    int addLockBalance(Long memberId, Integer coinId, BigDecimal amount);

    /**
     * 减可用余额
     * @param memberId 用户ID
     * @param coinId 币种ID
     * @param amount 金额
     * @return
     */
    int subLockBalance(Long memberId, Integer coinId, BigDecimal amount);

    /**
     * 通过用户ID和币种ID查询用户的钱包信息
     * @param memberId 用户ID
     * @param coinId 币种ID
     * @return
     */
    UmsMemberWallet findByMemberIdAndCoinId(Long memberId, Integer coinId);

    /**
     * 保存
     * @param umsMemberWallet
     */
    void saveMemberWallet(UmsMemberWallet umsMemberWallet);

    /**
     * 充值增加金额 直接增加可用金额
     * @param memberId 用户ID
     * @param coinId 币种ID
     * @param amount 金额
     */
    void depositBalance(Long memberId, Integer coinId, BigDecimal amount);
}
