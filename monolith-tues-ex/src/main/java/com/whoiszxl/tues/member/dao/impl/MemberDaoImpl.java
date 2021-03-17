package com.whoiszxl.tues.member.dao.impl;

import com.whoiszxl.tues.member.dao.MemberDao;
import com.whoiszxl.tues.member.entity.UmsMember;
import com.whoiszxl.tues.member.repository.UmsMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 用户dao层实现
 * 在repository与service层之间添加一层dao，增加扩展性。
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
@Repository
public class MemberDaoImpl implements MemberDao {

    @Autowired
    private UmsMemberRepository umsMemberRepository;

    @Override
    public UmsMember findUmsMemberByPhoneAndStatus(String phone, Integer status) {
        return umsMemberRepository.findUmsMemberByPhoneAndStatus(phone, status);
    }

    @Override
    public UmsMember findUmsMemberByIdAndStatus(Long id, Integer status) {
        return umsMemberRepository.findUmsMemberByIdAndStatus(id, status);
    }

    @Override
    public UmsMember save(UmsMember umsMember) {
        return umsMemberRepository.save(umsMember);
    }
}
