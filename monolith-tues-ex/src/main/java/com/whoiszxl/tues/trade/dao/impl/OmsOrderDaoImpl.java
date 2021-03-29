package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.OmsOrderDao;
import com.whoiszxl.tues.trade.entity.OmsOrder;
import com.whoiszxl.tues.trade.repository.OmsOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 挂单dao接口实现
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Service
public class OmsOrderDaoImpl implements OmsOrderDao {

    @Autowired
    private OmsOrderRepository omsOrderRepository;

    @Override
    public OmsOrder save(OmsOrder omsOrder) {
        return omsOrderRepository.save(omsOrder);
    }

    @Override
    public List<OmsOrder> getBuyMatchOrderList(Integer type, Long memberId, Integer coinId, Integer replaceCoinId, BigDecimal price) {
        return omsOrderRepository.getBuyMatchOrderList(type, memberId, coinId, replaceCoinId, price);
    }

    @Override
    public List<OmsOrder> getSellMatchOrderList(Integer type, Long memberId, Integer coinId, Integer replaceCoinId, BigDecimal price) {
        return omsOrderRepository.getSellMatchOrderList(type, memberId, coinId, replaceCoinId, price);
    }

    @Override
    public void changeCountAndStatus(BigDecimal orderCount, Integer status, Long id) {
        omsOrderRepository.changeCountAndStatus(orderCount, status, id);
    }

    @Override
    public List<OmsOrder> findAllByMemberIdOrderByIdDesc(Long memberId) {
        return omsOrderRepository.findAllByMemberIdOrderByIdDesc(memberId);
    }
}
