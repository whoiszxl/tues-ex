package com.whoiszxl.tues.trade.repository;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.trade.entity.OmsCoin;
import com.whoiszxl.tues.trade.entity.OmsDeposit;
import com.whoiszxl.tues.trade.entity.OmsHeight;

import java.util.List;

/**
 * 区块高度记录repository服务
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
public interface OmsHeightRepository extends BaseRepository<OmsHeight> {

    /**
     * 通过币种ID获取当前同步的高度
     * @param coinName 币种ID
     * @return 区块高度
     */
    OmsHeight findByCoinName(String coinName);
}
