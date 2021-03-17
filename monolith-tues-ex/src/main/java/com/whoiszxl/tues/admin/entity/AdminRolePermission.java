package com.whoiszxl.tues.admin.entity;

import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色与权限关联表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminRolePermission extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "权限ID")
    private Long perId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
