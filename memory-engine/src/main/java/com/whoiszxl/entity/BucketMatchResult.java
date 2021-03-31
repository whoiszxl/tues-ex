package com.whoiszxl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * bucket预撮合返回结果
 */
@Data
@AllArgsConstructor
public class BucketMatchResult {

    private List<ExDeal> dealList;

    private List<ExOrder> orderList;

    private BigDecimal allMatchCount;
}
