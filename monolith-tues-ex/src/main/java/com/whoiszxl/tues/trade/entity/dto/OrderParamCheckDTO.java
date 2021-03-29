package com.whoiszxl.tues.trade.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 挂单参数校验DTO
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Data
@Builder
public class OrderParamCheckDTO {

    private Integer checkCoinId;

    private BigDecimal orderBalance;

    private Integer buyOrSell;

}
