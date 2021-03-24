package com.whoiszxl.tues.member.dao.impl;

import com.whoiszxl.tues.member.dao.MemberAddressDao;
import com.whoiszxl.tues.member.dao.MemberDao;
import com.whoiszxl.tues.member.entity.UmsMember;
import com.whoiszxl.tues.member.entity.UmsMemberAddress;
import com.whoiszxl.tues.member.repository.UmsMemberAddressRepository;
import com.whoiszxl.tues.member.repository.UmsMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 用户地址dao层实现
 * 在repository与service层之间添加一层dao，增加扩展性。
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
@Repository
public class MemberAddressDaoImpl implements MemberAddressDao {

    @Autowired
    private UmsMemberAddressRepository umsMemberAddressRepository;

    @Override
    public UmsMemberAddress findByMemberIdAndCoinIdAndStatus(Long memberId, Integer coinId, Integer status) {
        return umsMemberAddressRepository.findByMemberIdAndCoinIdAndStatus(memberId, coinId, status);
    }

    @Override
    public UmsMemberAddress findByCoinIdAndDepositAddressAndStatus(Long coinId, String depositAddress, Integer status) {
        return umsMemberAddressRepository.findByCoinIdAndDepositAddressAndStatus(coinId, depositAddress, status);
    }

    @Override
    public UmsMemberAddress save(UmsMemberAddress umsMemberAddress) {
        return umsMemberAddressRepository.save(umsMemberAddress);
    }
}
