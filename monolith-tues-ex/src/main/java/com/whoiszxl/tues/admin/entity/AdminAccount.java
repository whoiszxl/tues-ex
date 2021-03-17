package com.whoiszxl.tues.admin.entity;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminAccount extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "真实姓名")
    private String realname;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "国家码")
    private String countryCode;

    @ApiModelProperty(value = "谷歌验证码")
    private String googleCode;

    @ApiModelProperty(value = "最后登录的IP")
    private String lastLoginIp;

    @ApiModelProperty(value = "最后登录")
    private LocalDateTime lastLogin;

    @ApiModelProperty(value = "关联的角色ID")
    private Long roleId;

    @ApiModelProperty(value = "状态(0：无效 1：有效)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
