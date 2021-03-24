package com.whoiszxl.tues.member.service.impl;

import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.member.dao.MemberAddressDao;
import com.whoiszxl.tues.member.entity.UmsMemberAddress;
import com.whoiszxl.tues.member.entity.dto.UmsMemberAddressDTO;
import com.whoiszxl.tues.member.service.MemberAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户地址服务
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
@Service
public class MemberAddressServiceImpl implements MemberAddressService {

    @Autowired
    private MemberAddressDao memberAddressDao;

    @Override
    public UmsMemberAddressDTO getMemberAddressByCoinId(Long memberId, Integer coinId) {
        UmsMemberAddress umsMemberAddress = memberAddressDao.findByMemberIdAndCoinIdAndStatus(memberId, coinId, SwitchStatusEnum.STATUS_OPEN.getStatusCode());
        if(umsMemberAddress != null) {
            return umsMemberAddress.clone(UmsMemberAddressDTO.class);
        }
        return null;

    }

    @Override
    public UmsMemberAddressDTO save(UmsMemberAddress umsMemberAddress) {
        return memberAddressDao.save(umsMemberAddress).clone(UmsMemberAddressDTO.class);
    }
}
