package com.whoiszxl.tues.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 * TODO 扩展实现
 * @author whoiszxl
 * @date 2021/3/17
 */
@Getter
@AllArgsConstructor
public enum MemberRoleEnum {
    ROLE_ADMIN("claims_admin", "admin"),
    ROLE_USER("claims_member", "member")
    ;
    private String roleAttrKey;
    private String roleName;
}