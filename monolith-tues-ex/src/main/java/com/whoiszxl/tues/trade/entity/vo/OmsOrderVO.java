package com.whoiszxl.tues.trade.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 挂单表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
public class OmsOrderVO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    @ApiModelProperty(value = "交易对名称")
    private String pairName;

    @ApiModelProperty(value = "交易对第一个币种ID")
    private Integer coinId;

    @ApiModelProperty(value = "交易对第二个币种ID")
    private Integer replaceCoinId;

    @ApiModelProperty(value = "委托价格")
    private BigDecimal price;

    @ApiModelProperty(value = "委托总数量")
    private BigDecimal totalCount;

    @ApiModelProperty(value = "当前可交易数量（挂单的金额可能超过当前所有挂单的总和）")
    private BigDecimal currentCount;

    @ApiModelProperty(value = "买卖方向 1：买入 -1：卖出")
    private Integer direction;

    @ApiModelProperty(value = "挂单类型 1：限价 2：市价")
    private Integer type;

    @ApiModelProperty(value = "0代表部分交易，可交易，1是所有已成交，交易结束， -1用户撤单")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间,挂单时间")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}
