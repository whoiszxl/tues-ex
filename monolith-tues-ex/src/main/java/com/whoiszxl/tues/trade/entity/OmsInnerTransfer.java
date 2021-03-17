package com.whoiszxl.tues.trade.entity;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 内部转账表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OmsInnerTransfer extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "币种ID")
    private Long coinId;

    @ApiModelProperty(value = "转账金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "转出方用户ID")
    private Long fromMemberId;

    @ApiModelProperty(value = "转入方用户ID")
    private Long toMemberId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}
