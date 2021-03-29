package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.OmsDealDao;
import com.whoiszxl.tues.trade.entity.OmsDeal;
import com.whoiszxl.tues.trade.repository.OmsDealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单成交记录dao接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Repository
public class OmsDealDaoImpl implements OmsDealDao {

    @Autowired
    private OmsDealRepository omsDealRepository;

    @Override
    public OmsDeal save(OmsDeal omsDeal) {
        return omsDealRepository.save(omsDeal);
    }

    @Override
    public List<OmsDeal> findAllByMemberIdAndOrderIdOrderByIdDesc(Long memberId, Long orderId) {
        return omsDealRepository.findAllByMemberIdAndOrderIdOrderByIdDesc(memberId, orderId);
    }
}
