package com.whoiszxl.tues.member.entity.param;

import lombok.Data;

/**
 * 登录请求参数
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
@Data
public class LoginParam {

    private String username;
    private String password;
}
