package com.whoiszxl.tues.member.service.impl;

import com.google.common.collect.Maps;
import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.enums.MemberGradeLevelEnum;
import com.whoiszxl.tues.common.enums.MemberRoleEnum;
import com.whoiszxl.tues.common.enums.MemberStatusEnum;
import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.enums.redis.MemberRedisPrefixEnum;
import com.whoiszxl.tues.common.exception.custom.JwtAuthException;
import com.whoiszxl.tues.common.utils.IdWorker;
import com.whoiszxl.tues.common.utils.JwtUtils;
import com.whoiszxl.tues.common.utils.RandomUtils;
import com.whoiszxl.tues.common.utils.RedisUtils;
import com.whoiszxl.tues.member.dao.MemberDao;
import com.whoiszxl.tues.member.entity.UmsMember;
import com.whoiszxl.tues.member.entity.dto.UmsMemberDTO;
import com.whoiszxl.tues.member.entity.param.RegisterParam;
import com.whoiszxl.tues.member.service.MemberService;
import com.whoiszxl.tues.thirdparty.sms.SMSProvider;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户服务实现类
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SMSProvider smsProvider;

    @Value("${aliyun.sms.mock}")
    private Boolean mock;

    @Override
    public boolean checkVerifyCode(String mobile, String memberVerifyCode) {
        String redisVerifyCode = redisUtils.get(MemberRedisPrefixEnum.USER_REGISTER_PHONE_CODE.getKey() + mobile);
        return StringUtils.equals(memberVerifyCode, redisVerifyCode);
    }

    @Override
    public void registerToDb(RegisterParam registerParam) {
        UmsMember member = new UmsMember();
        member.setId(idWorker.nextId());
        member.setUsername(registerParam.getMobile());
        member.setPassword(encoder.encode(registerParam.getPassword()));
        member.setPhone(registerParam.getMobile());
        member.setLoginCount(0);
        member.setLoginErrorCount(0);
        member.setTradeCount(0);
        member.setTradeFeeRate(new BigDecimal("0.001"));
        member.setGradeLevel(MemberGradeLevelEnum.LEVEL_ONE.getLevel());
        member.setDayWithdrawCount(1);
        member.setStatus(MemberStatusEnum.MEMBER_VAILD.getStatus());
        member.setUpdatedAt(LocalDateTime.now());
        member.setCreatedAt(LocalDateTime.now());
        memberDao.save(member);
    }

    @Override
    public UmsMemberDTO login(String username, String password) {
        UmsMember umsMember = memberDao.findUmsMemberByPhoneAndStatus(username, MemberStatusEnum.MEMBER_VAILD.getStatus());
        if(ObjectUtils.isNotEmpty(umsMember) && encoder.matches(password, umsMember.getPassword())) {
            //更新最后登录时间和登录次数
            umsMember.setLastLogin(LocalDateTime.now());
            umsMember.setUpdatedAt(LocalDateTime.now());
            umsMember.setLoginCount(umsMember.getLoginCount() + 1);
            UmsMember result = memberDao.save(umsMember);
            return result.clone(UmsMemberDTO.class);
        }else {
            //登录失败，更新错误次数
            umsMember.setUpdatedAt(LocalDateTime.now());
            umsMember.setLoginErrorCount(umsMember.getLoginErrorCount() + 1);
            memberDao.save(umsMember);
            return null;
        }
    }

    @Override
    public Map<String, Object> issueSign(UmsMemberDTO member, String roleName) {
        String token = jwtUtils.createJWT(member.getId().toString(), member.getUsername(), MemberRoleEnum.ROLE_USER.getRoleName());
        Map<String, Object> map = Maps.newHashMap();
        map.put("token", token);
        map.put("username", member.getUsername());
        map.put("countryCode", member.getCountryCode());
        return map;
    }

    @Override
    public void removeVerifyInRedis(String mobile) {
        redisUtils.delete(MemberRedisPrefixEnum.USER_REGISTER_PHONE_CODE.getKey() + mobile);
    }

    @Override
    public UmsMemberDTO getMemberInfoByMemberId(Long memberId) {
        UmsMember member = memberDao.findUmsMemberByIdAndStatus(memberId, SwitchStatusEnum.STATUS_OPEN.getStatusCode());
        return member.clone(UmsMemberDTO.class);
    }

    @Override
    public void register(RegisterParam registerParam) {
        //校验缓存中的验证码
        boolean isSuccess = checkVerifyCode(registerParam.getMobile(), registerParam.getCode());
        if(!isSuccess) {
            throw new JwtAuthException();
        }

        //入库并清除验证码
        registerToDb(registerParam);
        removeVerifyInRedis(registerParam.getMobile());
    }


    @Override
    public Result sendRegisterSms(String mobile) {

        //生成验证码
        String code = RandomUtils.generateNumberString(6);

        //存入redis
        redisUtils.setEx(MemberRedisPrefixEnum.USER_REGISTER_PHONE_CODE.getKey() + mobile,
                code,
                MemberRedisPrefixEnum.USER_REGISTER_PHONE_CODE.getTime(),
                MemberRedisPrefixEnum.USER_REGISTER_PHONE_CODE.getUnit());

        if(Boolean.FALSE.equals(mock)) {
           return smsProvider.sendVerifyMessage(mobile, code);
        }

        return Result.buildSuccess("mock send:" + code);
    }
}
