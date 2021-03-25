package com.whoiszxl.tues.thirdparty.sms;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.enums.redis.MemberRedisPrefixEnum;
import com.whoiszxl.tues.common.utils.RandomUtils;
import com.whoiszxl.tues.common.utils.RedisUtils;
import com.whoiszxl.tues.common.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 短信抽象工厂
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
@Component
public abstract class AbstractSMSFactory {

    @Autowired
    private RedisUtils redisUtils;

    @Value("${msg.mock}")
    private boolean msgMock;

    public Result sendVerifySms(String mobile) {
        //执行公共逻辑
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

        return specificSend(mobile, code);
    }


    protected abstract Result specificSend(String mobile, String code);

}
