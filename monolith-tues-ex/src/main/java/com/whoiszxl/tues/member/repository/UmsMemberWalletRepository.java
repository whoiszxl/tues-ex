package com.whoiszxl.tues.member.repository;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.member.entity.UmsMemberWallet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户钱包repository服务
 */
public interface UmsMemberWalletRepository extends BaseRepository<UmsMemberWallet> {

    @Modifying
    @Transactional
    @Query(value = "update ums_member_wallet " +
            "set lock_balance = lock_balance - ?3, usable_balance = usable_balance + ?3 " +
            "where member_id = ?1 and coin_id = ?2 and lock_balance >= ?3 and status = 1",
            nativeQuery = true)
    int unlockBalance(Long memberId, Integer coinId, BigDecimal amount);

    @Modifying
    @Transactional
    @Query(value = "update ums_member_wallet " +
                    "set lock_balance = lock_balance + ?3, usable_balance = usable_balance - ?3 " +
                    "where member_id = ?1 and coin_id = ?2 and usable_balance >= ?3 and status = 1",
    nativeQuery = true)
    int lockBalance(Long memberId, Integer coinId, BigDecimal amount);

    @Modifying
    @Transactional
    @Query(value = "update ums_member_wallet " +
                    "set usable_balance = usable_balance + ?3 " +
                    "where member_id = ?1 and coin_id = ?2 and status = 1",
                nativeQuery = true)
    int addBalance(Long memberId, Integer coinId, BigDecimal amount);

    @Modifying
    @Transactional
    @Query(value = "update ums_member_wallet " +
            "set usable_balance = usable_balance + ?3 " +
            "where member_id = ?1 and coin_id = ?2 and status = 1",
            nativeQuery = true)
    int depositBalance(Long memberId, Integer coinId, BigDecimal amount);

    @Modifying
    @Transactional
    @Query(value = "update ums_member_wallet " +
                    "set lock_balance = lock_balance + ?3 " +
                    "where member_id = ?1 and coin_id = ?2 and status = 1",
            nativeQuery = true)
    int addLockBalance(Long memberId, Integer coinId, BigDecimal amount);

    @Modifying
    @Transactional
    @Query(value = "update ums_member_wallet " +
            "set lock_balance = lock_balance - ?3 " +
            "where member_id = ?1 and coin_id = ?2 and lock_balance >= ?3 and status = 1",
            nativeQuery = true)
    int subLockBalance(Long memberId, Integer coinId, BigDecimal amount);

    /**
     * 通过用户ID和币种ID查询用户的钱包信息
     * @param memberId 用户ID
     * @param coinId 币种ID
     * @return
     */
    UmsMemberWallet findByMemberIdAndCoinId(Long memberId, Integer coinId);

    /**
     * 通过用户ID和状态查询用户的资产列表
     * @param memberId 用户ID
     * @param status 状态值
     * @return
     */
    List<UmsMemberWallet> findAllByMemberIdAndStatus(Long memberId, Integer status);
}
