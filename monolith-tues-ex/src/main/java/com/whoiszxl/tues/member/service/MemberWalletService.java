package com.whoiszxl.tues.member.service;

import java.math.BigDecimal;

public interface MemberWalletService {

    /**
     * 增加余额
     * @param memberId 用户ID
     * @param coinId 币种ID
     * @param depositActual 充值金额
     */
    void addBalance(Long memberId, Integer coinId, BigDecimal depositActual);
}
