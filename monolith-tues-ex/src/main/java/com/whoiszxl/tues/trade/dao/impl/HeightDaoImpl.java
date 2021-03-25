package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.HeightDao;
import com.whoiszxl.tues.trade.entity.OmsHeight;
import com.whoiszxl.tues.trade.repository.OmsHeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 区块高度dao实现
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Repository
public class HeightDaoImpl implements HeightDao {

    @Autowired
    private OmsHeightRepository omsHeightRepository;

    @Override
    public OmsHeight findByCoinName(String coinName) {
        return omsHeightRepository.findByCoinName(coinName);
    }

    @Override
    public OmsHeight save(OmsHeight height) {
        return omsHeightRepository.save(height);
    }
}
