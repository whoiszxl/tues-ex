package com.whoiszxl.tues.member.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录请求参数
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
@Data
public class LoginParam {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;
}
