package com.whoiszxl.tues.member.entity.vo;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UmsMemberVO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "国家码")
    private String countryCode;

    @ApiModelProperty(value = "谷歌验证码")
    private String googleKey;

    @ApiModelProperty(value = "谷歌验证码是否开启，默认不开启")
    private Integer googleStatus;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "区域")
    private String district;

    @ApiModelProperty(value = "用户等级")
    private String gradeLevel;

    @ApiModelProperty(value = "当前等级每日提现额度")
    private Integer dayWithdrawCount;

    @ApiModelProperty(value = "当前等级交易手续费率")
    private BigDecimal tradeFeeRate;

    @ApiModelProperty(value = "用户交易次数")
    private Integer tradeCount;

    @ApiModelProperty(value = "用户登录次数")
    private Integer loginCount;

    @ApiModelProperty(value = "用户登录错误次数")
    private Integer loginErrorCount;

    @ApiModelProperty(value = "状态(0：无效 1：有效)")
    private Integer status;

    @ApiModelProperty(value = "最后登录")
    private LocalDateTime lastLogin;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}
