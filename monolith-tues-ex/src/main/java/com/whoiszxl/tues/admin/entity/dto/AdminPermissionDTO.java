package com.whoiszxl.tues.admin.entity.dto;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员权限表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminPermissionDTO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "权限名称")
    private String perName;

    @ApiModelProperty(value = "权限值")
    private String perValue;

    @ApiModelProperty(value = "父权限ID")
    private Long parentId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "权限描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
