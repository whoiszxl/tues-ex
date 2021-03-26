package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.enums.BuySellEnum;
import com.whoiszxl.tues.member.dao.MemberWalletDao;
import com.whoiszxl.tues.trade.dao.OmsOrderDao;
import com.whoiszxl.tues.trade.dao.OmsTransactionDao;
import com.whoiszxl.tues.trade.entity.OmsTransaction;
import com.whoiszxl.tues.trade.service.MatchService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 撮合订单服务接口实现
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private OmsTransactionDao transactionDao;

    @Autowired
    private OmsOrderDao orderDao;

    @Autowired
    private MemberWalletDao memberWalletDao;

    /**
     * TODO 使用数据库撮合，后续更改为Java内存撮合
     * @param transaction 挂单信息
     */
    @Override
    public void matchOrder(OmsTransaction transactionData) {
        //查询其他人的挂单，并且交易方向是反向的记录
        List<OmsTransaction> data = transactionData.getType().equals(BuySellEnum.BUY.getValue()) ?
                transactionDao.getBuyMatchTransactionList(transactionData.getType(),
                        transactionData.getMemberId(),
                        transactionData.getCoinId(),
                        transactionData.getReplaceCoinId(),
                        transactionData.getPrice())
                :
                transactionDao.getSellMatchTransactionList(transactionData.getType(),
                        transactionData.getMemberId(),
                        transactionData.getCoinId(),
                        transactionData.getReplaceCoinId(),
                        transactionData.getPrice());

        if(ObjectUtils.isEmpty(data)) {
            return;
        }
    }
}
