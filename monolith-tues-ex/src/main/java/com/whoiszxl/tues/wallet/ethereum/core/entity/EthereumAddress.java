package com.whoiszxl.tues.wallet.ethereum.core.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 以太坊地址实体
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
@Data
@AllArgsConstructor
public class EthereumAddress {

    @ApiModelProperty("以太坊地址")
    private String address;

    @ApiModelProperty("秘钥名称")
    private String keystoreName;

    @ApiModelProperty("助记词")
    private String mnemonic;

}
