package com.whoiszxl.tues.trade.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DealListParam {

    @NotNull(message = "挂单ID不允许为空")
    @ApiModelProperty("挂单ID")
    private Long orderId;
}
