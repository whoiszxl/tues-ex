package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.trade.entity.OmsPair;

import java.util.List;

/**
 * 交易对dao接口
 */
public interface PairDao {

    /**
     * 通过状态查询交易对列表 并按sort与id排序
     * @param status 状态
     * @return
     */
    List<OmsPair> findAllByStatusOrderBySortAscIdAsc(Integer status);

}
