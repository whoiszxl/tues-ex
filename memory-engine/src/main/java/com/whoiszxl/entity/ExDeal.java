package com.whoiszxl.entity;

import lombok.Data;

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
public class ExDeal implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 交易对名称 */
    private String pairName;

    /** 买方订单ID */
    private Long buyOrderId;

    /** 卖方订单ID */
    private Long sellOrderId;

    /** 交易对第一个币种ID */
    private Integer coinId;

    /** 交易对第二个币种ID */
    private Integer replaceCoinId;

    /** 成交价格 */
    private BigDecimal price;

    /** 委托总数量 */
    private BigDecimal successCount;

    /** 买卖方向 */
    private Integer direction;

    /** 创建时间 */
    private LocalDateTime time;


}
