package com.whoiszxl.tues.member.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 注册请求参数
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
@Data
public class RegisterParam {

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("重复输入的密码")
    private String rePassword;
}
