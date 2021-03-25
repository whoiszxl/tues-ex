package com.whoiszxl.tues.trade.entity.param;

import lombok.Data;

/**
 * 分页请求实体
 *
 * @author whoiszxl
 * @date 2021/3/25
 */
@Data
public class PageParam {

    private Integer pageNumber = 0;

    private Integer pageSize = 10;

}
