package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.trade.entity.OmsCoin;

import java.util.List;

/**
 * 币种dao服务
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
public interface CoinDao {

    /**
     * 通过状态查询币种列表 - 按sort与id升序
     * @param status 状态值
     * @return
     */
    List<OmsCoin> findAllByStatusOrderBySortAscIdAsc(Integer status);
}
