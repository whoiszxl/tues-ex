package com.whoiszxl.tues.member.dao.impl;

import com.whoiszxl.tues.member.dao.MemberWalletDao;
import com.whoiszxl.tues.member.entity.UmsMemberWallet;
import com.whoiszxl.tues.member.repository.UmsMemberWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class MemberWalletDaoImpl implements MemberWalletDao {

    @Autowired
    private UmsMemberWalletRepository umsMemberWalletRepository;

    @Override
    public int lockBalance(Long memberId, Integer coinId, BigDecimal amount) {
        return umsMemberWalletRepository.lockBalance(memberId, coinId, amount);
    }

    @Override
    public int addBalance(Long memberId, Integer coinId, BigDecimal amount) {
        return umsMemberWalletRepository.addBalance(memberId, coinId, amount);
    }

    @Override
    public int addLockBalance(Long memberId, Integer coinId, BigDecimal amount) {
        return umsMemberWalletRepository.addLockBalance(memberId, coinId, amount);
    }

    @Override
    public int subLockBalance(Long memberId, Integer coinId, BigDecimal amount) {
        return umsMemberWalletRepository.subLockBalance(memberId, coinId, amount);
    }

    @Override
    public UmsMemberWallet findByMemberIdAndCoinId(Long memberId, Integer coinId) {
        return umsMemberWalletRepository.findByMemberIdAndCoinId(memberId, coinId);
    }

    @Override
    public void saveMemberWallet(UmsMemberWallet umsMemberWallet) {
        umsMemberWalletRepository.save(umsMemberWallet);
    }
}