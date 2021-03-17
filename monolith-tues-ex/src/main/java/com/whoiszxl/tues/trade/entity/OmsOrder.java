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
 * 订单成交表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OmsOrder extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "买家用户ID")
    private Long memberId;

    @ApiModelProperty(value = "挂单ID")
    private String transactionId;

    @ApiModelProperty(value = "交易对第一个币种ID")
    private Integer coinId;

    @ApiModelProperty(value = "交易对第二个币种ID")
    private Integer replaceCoinId;

    @ApiModelProperty(value = "成交价格")
    private BigDecimal price;

    @ApiModelProperty(value = "委托总数量")
    private BigDecimal successCount;

    @ApiModelProperty(value = "0：买入 1：卖出")
    private Integer type;

    @ApiModelProperty(value = "创建时间,成交时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}
