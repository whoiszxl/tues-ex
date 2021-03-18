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
 * 交易对表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OmsContract extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "交易对名称")
    private String contractName;

    @ApiModelProperty(value = "交易对类型：1:主流币 2:DeFi 3:存储")
    private Integer contractType;

    @ApiModelProperty(value = "交易对第一个币种ID")
    private Integer coinId;

    @ApiModelProperty(value = "交易对第二个币种ID")
    private Integer replaceCoinId;

    @ApiModelProperty(value = "购买者所出费率")
    private BigDecimal buyerFee;

    @ApiModelProperty(value = "出售者所出费率")
    private BigDecimal sellerFee;

    @ApiModelProperty(value = "最大挂单笔数")
    private Integer maxOrders;

    @ApiModelProperty(value = "展示顺序")
    private Integer sort;

    @ApiModelProperty(value = "交易对状态，0：关闭 1：开启")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}