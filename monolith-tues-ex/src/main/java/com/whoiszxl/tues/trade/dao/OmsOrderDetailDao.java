package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.trade.entity.OmsOrderDetail;

/**
 * 订单详情记录dao接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface OmsOrderDetailDao {


    /**
     * 保存订单详情
     * @param detail
     * @return
     */
    OmsOrderDetail save(OmsOrderDetail detail);
}
