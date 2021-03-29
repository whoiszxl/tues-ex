package com.whoiszxl.tues.trade.repository;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.trade.entity.OmsDeal;

import java.util.List;

/**
 * 挂单成交repository服务
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface OmsDealRepository extends BaseRepository<OmsDeal> {

    /**
     * 通过用户ID和挂单ID按照主键ID降序查询成交记录
     * @param memberId 用户ID
     * @param orderId 挂单ID
     * @return
     */
    List<OmsDeal> findAllByMemberIdAndOrderIdOrderByIdDesc(Long memberId, Long orderId);

}
