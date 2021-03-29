package com.whoiszxl.tues.trade.entity.dto;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
public class OmsDealDTO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "买家用户ID")
    private Long memberId;

    @ApiModelProperty(value = "挂单ID")
    private Long orderId;

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
