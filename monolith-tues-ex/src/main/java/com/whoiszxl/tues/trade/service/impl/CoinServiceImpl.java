package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.trade.dao.CoinDao;
import com.whoiszxl.tues.trade.entity.OmsCoin;
import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.common.bean.PageParam;
import com.whoiszxl.tues.trade.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public OmsCoinDTO findCoinByName(String name) {
        //获取币种信息
        List<OmsCoinDTO> coinDTOList = findAllByStatusOrderBySortAscIdAsc(SwitchStatusEnum.STATUS_OPEN.getStatusCode());
        Optional<OmsCoinDTO> coinOptional = coinDTOList.stream().filter(item -> item.getCoinName().equals(name)).findFirst();
        if(coinOptional.isPresent()) {
            return coinOptional.get();
        }
        return null;
    }

    @Override
    public List<OmsCoinDTO> coinList(PageParam pageParam) {
        Pageable pageable = PageRequest.of(pageParam.getPageNumber(), pageParam.getPageSize());
        Page<OmsCoin> coinPage = coinDao.findAllByStatusOrderBySortAscIdAsc(SwitchStatusEnum.STATUS_OPEN.getStatusCode(), pageable);
        List<OmsCoin> omsCoinList = coinPage.getContent();
        return BeanCopierUtils.copyListProperties(omsCoinList, OmsCoinDTO::new);
    }
}
