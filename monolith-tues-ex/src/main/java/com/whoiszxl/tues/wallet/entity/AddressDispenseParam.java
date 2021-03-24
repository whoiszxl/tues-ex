package com.whoiszxl.tues.wallet.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 地址分配请求参数
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Data
public class AddressDispenseParam implements Serializable {


    @NotNull(message = "货币ID不能为空")
    @ApiModelProperty("货币ID")
    private Integer coinId;

}
