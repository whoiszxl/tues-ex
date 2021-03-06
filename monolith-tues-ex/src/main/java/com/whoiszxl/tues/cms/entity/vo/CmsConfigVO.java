package com.whoiszxl.tues.cms.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 配置表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
public class CmsConfigVO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置键")
    private String key;

    @ApiModelProperty(value = "配置值")
    private String value;

    @ApiModelProperty(value = "配置说明")
    private String remark;

    @ApiModelProperty(value = "配置状态，0：关闭 1：开启")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

}
