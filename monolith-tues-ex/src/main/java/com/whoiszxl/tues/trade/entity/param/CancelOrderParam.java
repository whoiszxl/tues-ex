package com.whoiszxl.tues.trade.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 撤单参数
 *
 * @author whoiszxl
 * @date 2021/3/26
 */
@Data
public class CancelOrderParam {

    @ApiModelProperty("订单ID")
    private Long orderId;
}
