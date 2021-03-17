package com.whoiszxl.tues.member.controller;

import com.aliyuncs.CommonResponse;
import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.bean.StatusCode;
import com.whoiszxl.tues.common.enums.MemberRoleEnum;
import com.whoiszxl.tues.common.enums.redis.MemberRedisPrefixEnum;
import com.whoiszxl.tues.common.exception.custom.JwtAuthException;
import com.whoiszxl.tues.common.utils.*;
import com.whoiszxl.tues.member.entity.dto.UmsMemberDTO;
import com.whoiszxl.tues.member.entity.param.LoginParam;
import com.whoiszxl.tues.member.entity.param.RegisterParam;
import com.whoiszxl.tues.member.entity.param.SmsParam;
import com.whoiszxl.tues.member.entity.vo.UmsMemberVO;
import com.whoiszxl.tues.member.service.MemberService;
import com.whoiszxl.tues.thirdparty.sms.SMSProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户会员接口
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
@RestController
@CrossOrigin
@RequestMapping("/member")
@Api(tags = "用户会员接口")
public class MemberController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MemberService memberService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SMSProvider smsProvider;

    /**
     * 发送短信接口
     * @param smsRequest mobile 手机号
     * @return
     */
    @ApiOperation(value = "发送短信接口", notes = "发送短信接口", response = CommonResponse.class)
    @PostMapping("/sendVerifySms")
    public Result<CommonResponse> sendVerifySms(@RequestBody SmsParam smsParam) {
        String mobile = smsParam.getMobile();
        //对手机号进行校验
        if(!RegexUtils.checkPhone(mobile)) {
            return Result.buildError();
        }

        //生成验证码
        String code = RandomUtils.generateNumberString(6);

        //存入redis
        redisUtils.setEx(MemberRedisPrefixEnum.USER_REGISTER_PHONE_CODE.getKey() + mobile,
                code,
                MemberRedisPrefixEnum.USER_REGISTER_PHONE_CODE.getTime(),
                MemberRedisPrefixEnum.USER_REGISTER_PHONE_CODE.getUnit());

        Result result = smsProvider.sendVerifyMessage(mobile, code);
        return result;
    }

    @ApiOperation(value = "查询当前用户信息", notes = "通过JWT查询", response = UmsMemberVO.class)
    @PostMapping("/info")
    public Result<UmsMemberVO> info() throws Exception {
        String memberId = JwtUtils.getUserClaims(request).getId();
        UmsMemberVO umsMemberVO = memberService.getMemberInfoByMemberId(Long.parseLong(memberId)).clone(UmsMemberVO.class);
        return Result.buildSuccess(umsMemberVO);
    }

    /**
     * 注册
     * @return
     */
    @ApiOperation(value = "注册", notes = "通过手机验证码注册", response = UmsMemberVO.class)
    @PostMapping(value = "/register")
    public Result register(@RequestBody RegisterParam registerParam){
        //校验手机号的密码
        ValidateUtils.checkPasswordEqual(registerParam.getPassword(), registerParam.getRePassword());
        ValidateUtils.checkPasswordLevel(registerParam.getPassword());
        ValidateUtils.checkPhoneRegex(registerParam.getMobile());

        //校验缓存中的验证码
        boolean isSuccess = memberService.checkVerifyCode(registerParam.getMobile(), registerParam.getCode());
        if(!isSuccess) {
            throw new JwtAuthException();
        }

        //入库并清除验证码
        memberService.registerToDb(registerParam);
        memberService.removeVerifyInRedis(registerParam.getMobile());

        return Result.buildSuccess("注册成功");
    }

    @ApiOperation(value = "登录", notes = "使用手机号或用户名与密码进行登录", response = Map.class)
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginParam loginParam){
        //校验手机号的密码
        ValidateUtils.checkPasswordLevel(loginParam.getPassword());
        ValidateUtils.checkPhoneRegex(loginParam.getUsername());

        UmsMemberDTO memberResult = memberService.login(loginParam.getUsername(), loginParam.getPassword());
        if(memberResult == null) {
            return Result.buildError(StatusCode.LOGINERROR, "登录失败");
        }
        Map<String, Object> result = memberService.issueSign(memberResult, MemberRoleEnum.ROLE_USER.getRoleName());
        return Result.buildSuccess(result);
    }

}
