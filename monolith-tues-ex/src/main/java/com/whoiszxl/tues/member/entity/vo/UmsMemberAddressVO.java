package com.whoiszxl.tues.member.entity.vo;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户充值地址表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UmsMemberAddressVO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    @ApiModelProperty(value = "币种ID")
    private Integer coinId;

    @ApiModelProperty(value = "充值地址")
    private String depositAddress;

    @ApiModelProperty(value = "私钥")
    private String privateKey;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "keystore")
    private String keystore;

    @ApiModelProperty(value = "钱包状态，0：关闭 1：开启")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
