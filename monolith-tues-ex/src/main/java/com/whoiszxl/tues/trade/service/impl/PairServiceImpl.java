package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.trade.dao.PairDao;
import com.whoiszxl.tues.trade.entity.OmsPair;
import com.whoiszxl.tues.trade.entity.dto.OmsPairDTO;
import com.whoiszxl.tues.trade.service.PairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易对服务接口实现
 */
@Service
public class PairServiceImpl implements PairService {

    @Autowired
    private PairDao pairDao;

    @Override
    public List<OmsPairDTO> pairList() {
        List<OmsPair> omsPairList = pairDao.findAllByStatusOrderBySortAscIdAsc(SwitchStatusEnum.STATUS_OPEN.getStatusCode());
        List<OmsPairDTO> pairDTOList = BeanCopierUtils.copyListProperties(omsPairList, OmsPairDTO::new);
        return pairDTOList;
    }
}
