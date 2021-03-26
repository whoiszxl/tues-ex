package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.dto.OmsWithdrawalDTO;
import com.whoiszxl.tues.common.bean.PageParam;

import java.util.List;

/**
 * 提现服务接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface WithdrawalService {


    /**
     * 通过用户ID分页查询提现记录列表
     * @param memberId 用户ID
     * @param pageParam 分页参数
     * @return
     */
    List<OmsWithdrawalDTO> withdrawList(Long memberId, PageParam pageParam);
}
