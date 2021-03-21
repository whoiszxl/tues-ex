package com.whoiszxl.tues.cms.entity.dto;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * app版本管理表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
public class CmsAppVersionDTO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "下载地址")
    private String downloadUrl;

    @ApiModelProperty(value = "平台，类型字符串 IOS && ANDROID")
    private Integer platform;

    @ApiModelProperty(value = "版本号")
    private String appVersion;

    @ApiModelProperty(value = "更新标题")
    private String appTitle;

    @ApiModelProperty(value = "更新内容")
    private String appContent;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
