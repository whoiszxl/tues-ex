package com.whoiszxl.tues.trade.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 交易参数
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Data
public class OrderParam {

    @ApiModelProperty("用户ID")
    private Long memberId;

    @ApiModelProperty("交易价格")
    private BigDecimal price;

    @ApiModelProperty("交易数量")
    private BigDecimal count;

    @ApiModelProperty("交易对ID")
    private Long pairId;

    @ApiModelProperty(value = "交易类型  1:买入 -1:卖出")
    private Integer type;
}
