package com.whoiszxl.repository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 交易对表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@Table(name = "oms_pair")
@Entity
public class OmsPair implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    private Long id;

    /** 交易对名称 */
    private String pairName;

    /** 交易对类型：1:主流币 2:DeFi 3:存储 */
    private Integer pairType;

    /** 交易对第一个币种ID */
    private Integer coinId;

    /** 交易对第二个币种ID */
    private Integer replaceCoinId;

    /** 交易对第一个币种精度 */
    private Integer coinDecimals;

    /** 交易对第二个币种精度 */
    private Integer replaceCoinDecimals;

    /** 购买者所出费率 */
    private BigDecimal buyerFee;

    /** 出售者所出费率 */
    private BigDecimal sellerFee;

    /** 最大挂单笔数 */
    private Integer maxOrders;

    /** 展示顺序 */
    private Integer sort;

    /** 交易对状态，0：关闭 1：开启 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

}
