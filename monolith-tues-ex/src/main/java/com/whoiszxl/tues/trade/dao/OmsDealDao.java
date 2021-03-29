package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.trade.entity.OmsDeal;

import java.util.List;

/**
 * 订单成交记录dao接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface OmsDealDao {

    /**
     * 保存成交记录
     * @param omsDeal
     * @return
     */
    OmsDeal save(OmsDeal omsDeal);

    /**
     * 通过用户ID和挂单ID按照主键ID降序查询成交记录
     * @param memberId 用户ID
     * @param orderId 挂单ID
     * @return
     */
    List<OmsDeal> findAllByMemberIdAndOrderIdOrderByIdDesc(Long memberId, Long orderId);
}
