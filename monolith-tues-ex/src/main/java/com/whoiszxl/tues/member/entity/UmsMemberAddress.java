package com.whoiszxl.tues.member.entity;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "ums_member_address")
@Entity
public class UmsMemberAddress extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "主键ID")
    private Long id;

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
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
