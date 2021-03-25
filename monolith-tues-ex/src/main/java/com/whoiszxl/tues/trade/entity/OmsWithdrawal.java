package com.whoiszxl.tues.trade.entity;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户提现记录表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@Table(name = "oms_withdrawal")
@Entity
public class OmsWithdrawal extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    @ApiModelProperty(value = "币种ID")
    private Integer coinId;

    @ApiModelProperty(value = "货币名称")
    private String coinName;

    @ApiModelProperty(value = "提现交易hash")
    private String txHash;

    @ApiModelProperty(value = "总提现额")
    private BigDecimal withdrawAll;

    @ApiModelProperty(value = "提现手续费")
    private BigDecimal withdrawFee;

    @ApiModelProperty(value = "用户实际获得的提现金额")
    private BigDecimal withdrawActual;

    @ApiModelProperty(value = "交易所出币地址(BTC系列为从节点，所以为空)")
    private String fromAddress;

    @ApiModelProperty(value = "用户提币后收币地址")
    private String toAddress;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditAt;

    @ApiModelProperty(value = "审核操作人(管理员）")
    private String auditUid;

    @ApiModelProperty(value = "审核状态，0：审核不通过 1：审核通过 2：审核中")
    private Integer auditStatus;

    @ApiModelProperty(value = "上链时间")
    private LocalDateTime upchainAt;

    @ApiModelProperty(value = "上链成功时间")
    private LocalDateTime upchainSuccessAt;

    @ApiModelProperty(value = "上链状态，0：失败 1：成功 2：上链后等待确认中")
    private Integer upchainStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}
