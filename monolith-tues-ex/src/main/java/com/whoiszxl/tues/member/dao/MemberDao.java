package com.whoiszxl.tues.member.dao;

import com.whoiszxl.tues.member.entity.UmsMember;

/**
 * 用户Dao层接口
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
public interface MemberDao {

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


    /**
     * 更新用户
     * @param umsMember 用户参数
     * @return
     */
    UmsMember save(UmsMember umsMember);

}
