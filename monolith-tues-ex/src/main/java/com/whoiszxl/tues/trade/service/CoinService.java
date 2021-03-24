package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;

import java.util.List;

/**
 * TODO
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
public interface CoinService {

    /**
     * 通过状态查询币种列表 - 按sort与id升序
     * @param status 状态值
     * @return
     */
    List<OmsCoinDTO> findAllByStatusOrderBySortAscIdAsc(Integer status);
}
