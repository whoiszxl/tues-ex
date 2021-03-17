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
 * 币种表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OmsCoin extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "货币名称")
    private String coinName;

    @ApiModelProperty(value = "英文标识")
    private String coinMark;

    @ApiModelProperty(value = "货币logo")
    private String coinLogo;

    @ApiModelProperty(value = "货币类型： mainnet：主网币 token：代币")
    private String coinType;

    @ApiModelProperty(value = "币种描述")
    private String coinContent;

    @ApiModelProperty(value = "币总数量")
    private BigDecimal coinTotalNum;

    @ApiModelProperty(value = "币种小数位")
    private Integer coinDecimalsNum;

    @ApiModelProperty(value = "买入手续费")
    private BigDecimal coinBuyFee;

    @ApiModelProperty(value = "卖出手续费")
    private BigDecimal coinSellFee;

    @ApiModelProperty(value = "该币种的链接地址")
    private String coinUrl;

    @ApiModelProperty(value = "智能合约abi接口")
    private String contractAbi;

    @ApiModelProperty(value = "智能合约地址")
    private String contractAddress;

    @ApiModelProperty(value = "rpc路径")
    private String rpcUrl;

    @ApiModelProperty(value = "rpc用户名")
    private String rpcUsername;

    @ApiModelProperty(value = "rpc密码")
    private String rpcPassword;

    @ApiModelProperty(value = "最大提币额")
    private BigDecimal maxWithdraw;

    @ApiModelProperty(value = "最小提币额")
    private BigDecimal minWithdraw;

    @ApiModelProperty(value = "提币手续费")
    private BigDecimal feeWithdraw;

    @ApiModelProperty(value = "钱包储存路径")
    private String walletUrl;

    @ApiModelProperty(value = "钱包密钥")
    private String walletKey;

    @ApiModelProperty(value = "充值确认数")
    private Integer confirms;

    @ApiModelProperty(value = "展示顺序")
    private Integer sort;

    @ApiModelProperty(value = "币种状态，0：关闭 1：开启")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}
