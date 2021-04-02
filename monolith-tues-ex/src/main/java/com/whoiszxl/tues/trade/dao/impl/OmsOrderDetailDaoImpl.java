package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.OmsOrderDetailDao;
import com.whoiszxl.tues.trade.entity.OmsOrderDetail;
import com.whoiszxl.tues.trade.repository.OmsOrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 订单详情dao实现
 *
 * @author whoiszxl
 * @date 2021/4/2
 */
@Repository
public class OmsOrderDetailDaoImpl implements OmsOrderDetailDao {

    @Autowired
    private OmsOrderDetailRepository omsOrderDetailRepository;

    @Override
    public OmsOrderDetail save(OmsOrderDetail detail) {
        return omsOrderDetailRepository.saveAndFlush(detail);
    }
}
