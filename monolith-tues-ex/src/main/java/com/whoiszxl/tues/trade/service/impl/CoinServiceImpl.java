package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.trade.dao.CoinDao;
import com.whoiszxl.tues.trade.entity.OmsCoin;
import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.trade.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 币种服务实现
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinDao coinDao;

    @Override
    public List<OmsCoinDTO> findAllByStatusOrderBySortAscIdAsc(Integer status) {
        List<OmsCoin> coinList = coinDao.findAllByStatusOrderBySortAscIdAsc(SwitchStatusEnum.STATUS_OPEN.getStatusCode());
        return BeanCopierUtils.copyListProperties(coinList, OmsCoinDTO::new);
    }
}
