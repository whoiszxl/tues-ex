package com.whoiszxl.tues.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上链状态枚举
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Getter
@AllArgsConstructor
public enum UpchainStatusEnum {

    SUCCESS("上链并确认成功", 1),
    WAITING_CONFIRM("等待确认中", 2),
    NOT_UPCHAIN("未上链", 3);

    private String msg;
    private Integer code;
}
