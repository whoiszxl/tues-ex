package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.CoinDao;
import com.whoiszxl.tues.trade.entity.OmsCoin;
import com.whoiszxl.tues.trade.repository.OmsCoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 币种dao服务实现
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
@Repository
public class CoinDaoImpl implements CoinDao {

    @Autowired
    private OmsCoinRepository omsCoinRepository;

    @Override
    public List<OmsCoin> findAllByStatusOrderBySortAscIdAsc(Integer status) {
        return omsCoinRepository.findAllByStatusOrderBySortAscIdAsc(status);
    }
}
