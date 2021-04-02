package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.OmsPair;
import com.whoiszxl.tues.trade.entity.dto.OmsPairDTO;

import java.util.List;

/**
 * 交易对服务接口
 */
public interface PairService {

    /**
     * 获取交易对列表
     * @return 交易对列表
     */
    List<OmsPairDTO> pairList();

    /**
     * 通过交易对名获取交易对信息
     * @param pairName 交易对名称
     * @return
     */
    OmsPairDTO getPairByName(String pairName);
}
