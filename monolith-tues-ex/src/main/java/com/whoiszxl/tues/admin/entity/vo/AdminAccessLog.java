package com.whoiszxl.tues.admin.entity.vo;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员访问日志表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminAccessLog extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "访问IP")
    private String accessIp;

    @ApiModelProperty(value = "访问的方法")
    private String accessMethod;

    @ApiModelProperty(value = "访问的模块")
    private Integer accessModule;

    @ApiModelProperty(value = "访问的时间")
    private LocalDateTime accessTime;

    @ApiModelProperty(value = "访问的管理员ID")
    private Long adminId;

    @ApiModelProperty(value = "操作")
    private String operation;

    @ApiModelProperty(value = "访问路径")
    private String uri;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
