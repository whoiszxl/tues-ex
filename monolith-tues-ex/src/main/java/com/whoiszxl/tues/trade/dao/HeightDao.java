package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.trade.entity.OmsHeight;

/**
 * 高度dao接口
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
public interface HeightDao {

    /**
     * 通过币种ID获取当前同步的高度
     * @param coinName 币种ID
     * @return 区块高度
     */
    OmsHeight findByCoinName(String coinName);

    /**
     * 新增或更新
     * @param height
     * @return
     */
    OmsHeight save(OmsHeight height);
}
