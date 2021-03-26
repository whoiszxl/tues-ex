package com.whoiszxl.tues.member.service;

import com.whoiszxl.tues.member.entity.dto.UmsMemberWalletDTO;

import java.math.BigDecimal;
import java.util.List;

public interface MemberWalletService {

    /**
     * 增加余额
     * @param memberId 用户ID
     * @param coinId 币种ID
     * @Param coinName 币种名称
     * @param depositActual 充值金额
     */
    void addBalance(Long memberId, Integer coinId, String coinName, BigDecimal depositActual);

    /**
     * 获取用户资产列表
     * @param memberId
     * @return
     */
    List<UmsMemberWalletDTO> getAssetList(Long memberId);
}
