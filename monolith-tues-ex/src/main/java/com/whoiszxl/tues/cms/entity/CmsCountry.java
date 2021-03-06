package com.whoiszxl.tues.cms.entity;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "cms_country")
@Entity
public class CmsCountry extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "主键ID")
    private Long id;

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
