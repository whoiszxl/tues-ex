package com.whoiszxl.repository;

import java.util.List;

/**
 * 交易对repository接口
 *
 * @author zhouxiaolong
 * @date 2021/3/30
 */
public interface OmsPairRepository extends BaseRepository<OmsPair> {


    /**
     * 通过状态查询交易对列表 并按sort与id排序
     * @param status 状态
     * @return
     */
    List<OmsPair> findAllByStatusOrderBySortAscIdAsc(Integer status);
}
