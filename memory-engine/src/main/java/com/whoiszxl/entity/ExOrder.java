package com.whoiszxl.entity;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单簿中的订单实体
 */
@Data
public class ExOrder implements Serializable {

    /** 订单ID */
    private Long orderId;

    /** 用户ID */
    private Long memberId;

    /** 交易方向 */
    private Integer direction;

    /** 交易对名称 */
    private String pairName;

    /** 交易对第一个币种ID */
    private Integer coinId;

    /** 交易对第二个币种ID */
    private Integer replaceCoinId;

    /** 委托价格 */
    private BigDecimal price;

    /** 委托总数量 */
    private BigDecimal totalCount;

    /** 当前可交易数量 */
    private BigDecimal currentCount;

    /** 当前成交额(每一笔的成交量*成交金额累加数) */
    private BigDecimal turnover;

    /** 成交量 */
    private BigDecimal volume;

    /** 挂单时间 */
    private LocalDateTime addTime;

    /** 完成撮合时间 */
    private LocalDateTime complatedTime;

    /** 取消挂单时间 */
    private LocalDateTime canceledTime;

}
