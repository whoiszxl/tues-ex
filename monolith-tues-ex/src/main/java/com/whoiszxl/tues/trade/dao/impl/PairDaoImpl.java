package com.whoiszxl.tues.trade.dao.impl;

import com.whoiszxl.tues.trade.dao.PairDao;
import com.whoiszxl.tues.trade.entity.OmsPair;
import com.whoiszxl.tues.trade.repository.OmsPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 交易对Dao接口实现
 */
@Repository
public class PairDaoImpl implements PairDao {

    @Autowired
    private OmsPairRepository omsPairRepository;

    @Override
    public List<OmsPair> findAllByStatusOrderBySortAscIdAsc(Integer status) {
        return omsPairRepository.findAllByStatusOrderBySortAscIdAsc(status);
    }
}
