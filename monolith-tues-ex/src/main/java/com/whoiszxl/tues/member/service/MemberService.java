package com.whoiszxl.tues.member.service;

import com.aliyuncs.CommonResponse;
import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.member.entity.UmsMember;
import com.whoiszxl.tues.member.entity.dto.UmsMemberDTO;
import com.whoiszxl.tues.member.entity.param.RegisterParam;
import com.whoiszxl.tues.member.entity.param.SmsParam;

import java.util.Map;

/**
 * TODO
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
public interface MemberService {

    /**
     * 校验当前手机输入的验证码是否正确
     * @param mobile 用户手机号
     * @param memberVerifyCode 用户输入的验证码
     * @return
     */
    boolean checkVerifyCode(String mobile, String memberVerifyCode);

    /**
     * 注册到数据库中
     * @param registerParam 注册请求参数
     */
    void registerToDb(RegisterParam registerParam);

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    UmsMemberDTO login(String username, String password);

    /**
     * 签名
     * @param umsMemberDTO 用户信息
     * @param roleName 权限名
     * @return
     */
    Map<String,Object> issueSign(UmsMemberDTO umsMemberDTO, String roleName);

    /**
     * 从redis移除验证码
     * @param mobile
     */
    void removeVerifyInRedis(String mobile);

    /**
     * 通过用户ID获取用户信息
     * @param memberId 用户ID
     * @return
     */
    UmsMemberDTO getMemberInfoByMemberId(Long memberId);

    /**
     * 注册逻辑
     * @param registerParam 注册参数
     */
    void register(RegisterParam registerParam);

    /**
     * 发送注册短信
     * @param mobile 短信参数
     * @return
     */
    Result<Boolean> sendRegisterSms(String mobile);
}
