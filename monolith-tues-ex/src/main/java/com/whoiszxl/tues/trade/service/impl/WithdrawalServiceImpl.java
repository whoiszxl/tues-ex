package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.trade.dao.WithdrawalDao;
import com.whoiszxl.tues.trade.entity.OmsWithdrawal;
import com.whoiszxl.tues.trade.entity.dto.OmsWithdrawalDTO;
import com.whoiszxl.tues.common.bean.PageParam;
import com.whoiszxl.tues.trade.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提现服务接口实现
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    @Autowired
    private WithdrawalDao withdrawalDao;

    @Override
    public List<OmsWithdrawalDTO> withdrawList(Long memberId, PageParam pageParam) {
        Pageable pageable = PageRequest.of(pageParam.getPageNumber(), pageParam.getPageSize());
        Page<OmsWithdrawal> withdrawalPage = withdrawalDao.findAllByMemberIdOrderById(memberId, pageable);
        List<OmsWithdrawal> withdrawalList = withdrawalPage.getContent();
        return BeanCopierUtils.copyListProperties(withdrawalList, OmsWithdrawalDTO::new);
    }
}
