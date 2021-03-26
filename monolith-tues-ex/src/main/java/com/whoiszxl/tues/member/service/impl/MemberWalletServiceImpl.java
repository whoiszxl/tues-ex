package com.whoiszxl.tues.member.service.impl;

import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.common.utils.DateProvider;
import com.whoiszxl.tues.common.utils.IdWorker;
import com.whoiszxl.tues.member.dao.MemberWalletDao;
import com.whoiszxl.tues.member.entity.UmsMemberWallet;
import com.whoiszxl.tues.member.entity.dto.UmsMemberWalletDTO;
import com.whoiszxl.tues.member.service.MemberWalletService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MemberWalletServiceImpl implements MemberWalletService {

    @Autowired
    private MemberWalletDao memberWalletDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private DateProvider dateProvider;

    @Override
    public void addBalance(Long memberId, Integer coinId, String coinName, BigDecimal depositActual) {
        UmsMemberWallet memberWallet = memberWalletDao.findByMemberIdAndCoinId(memberId, coinId);
        if(ObjectUtils.isEmpty(memberWallet)) {
            //创建用户钱包信息
            UmsMemberWallet umsMemberWallet = new UmsMemberWallet();
            umsMemberWallet.setId(idWorker.nextId());
            umsMemberWallet.setCoinId(coinId);
            umsMemberWallet.setCoinName(coinName);
            umsMemberWallet.setMemberId(memberId);
            umsMemberWallet.setLockBalance(BigDecimal.ZERO);
            umsMemberWallet.setUsableBalance(depositActual);
            umsMemberWallet.setStatus(SwitchStatusEnum.STATUS_OPEN.getStatusCode());
            umsMemberWallet.setCreatedAt(dateProvider.now());
            umsMemberWallet.setUpdatedAt(dateProvider.now());
            memberWalletDao.saveMemberWallet(umsMemberWallet);
        }else {
            //直接更新
            memberWalletDao.addBalance(memberId, coinId, depositActual);
        }
    }

    @Override
    public List<UmsMemberWalletDTO> getAssetList(Long memberId) {
        List<UmsMemberWallet> memberWalletList = memberWalletDao.findAllByMemberIdAndStatus(memberId, SwitchStatusEnum.STATUS_OPEN.getStatusCode());
        return BeanCopierUtils.copyListProperties(memberWalletList, UmsMemberWalletDTO::new);
    }
}
