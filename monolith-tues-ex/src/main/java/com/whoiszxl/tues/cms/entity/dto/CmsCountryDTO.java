package com.whoiszxl.tues.cms.entity.dto;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 国家表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
public class CmsCountryDTO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "中文名")
    private String zhName;

    @ApiModelProperty(value = "区域码")
    private String areaCode;

    @ApiModelProperty(value = "英文名")
    private String enName;

    @ApiModelProperty(value = "语言编码")
    private String language;

    @ApiModelProperty(value = "当地币种编码")
    private String localCurrency;

    @ApiModelProperty(value = "排序")
    private Integer sort;


}
