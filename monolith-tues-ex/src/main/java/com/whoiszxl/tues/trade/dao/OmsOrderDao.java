package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.trade.entity.OmsOrder;

/**
 * 订单成交记录dao接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface OmsOrderDao {

    /**
     * 保存成交记录
     * @param omsOrder
     * @return
     */
    OmsOrder save(OmsOrder omsOrder);
}
