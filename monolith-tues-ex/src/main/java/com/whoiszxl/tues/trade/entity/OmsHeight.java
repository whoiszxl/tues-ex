package com.whoiszxl.tues.trade.entity;

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
 * 区块高度同步记录表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@Table(name = "oms_height")
@Entity
public class OmsHeight extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "主键币种ID")
    private Integer coinId;

    @ApiModelProperty(value = "货币名称")
    private String coinName;

    @ApiModelProperty(value = "当前服务扫描区块高度")
    private Long height;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}
