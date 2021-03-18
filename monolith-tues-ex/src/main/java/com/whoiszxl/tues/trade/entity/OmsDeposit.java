package com.whoiszxl.tues.trade.entity;

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
 * 用户充值记录表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OmsDeposit extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    @ApiModelProperty(value = "币种ID")
    private Integer coinId;

    @ApiModelProperty(value = "货币名称")
    private String coinName;

    @ApiModelProperty(value = "充值的交易hash")
    private String txHash;

    @ApiModelProperty(value = "用户实际充值的金额")
    private BigDecimal depositActual;

    @ApiModelProperty(value = "用户的出币地址")
    private String fromAddress;

    @ApiModelProperty(value = "交易所分配给用户的唯一地址")
    private String toAddress;

    @ApiModelProperty(value = "上链时间")
    private LocalDateTime upchainAt;

    @ApiModelProperty(value = "上链成功时间")
    private LocalDateTime upchainSuccessAt;

    @ApiModelProperty(value = "上链状态，0：失败 1：成功 2：上链后等待确认中")
    private Boolean upchainStatus;

    @ApiModelProperty(value = "当前交易所处区块的高度")
    private Integer height;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}