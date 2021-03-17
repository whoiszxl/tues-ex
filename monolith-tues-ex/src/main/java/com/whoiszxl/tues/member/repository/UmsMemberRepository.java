package com.whoiszxl.tues.member.repository;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.member.entity.UmsMember;

/**
 * 用户repository服务
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
public interface UmsMemberRepository extends BaseRepository<UmsMember> {

    /**
     * 通过手机号和状态查找用户
     * @param phone 手机号
     * @param status 状态
     * @return
     */
    UmsMember findUmsMemberByPhoneAndStatus(String phone, Integer status);

    /**
     * 通过主键ID和状态查找用户
     * @param id
     * @param status
     * @return
     */
    UmsMember findUmsMemberByIdAndStatus(Long id, Integer status);

}
