package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.param.TransactionParam;

/**
 * 挂单服务
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface TransactionService {

    /**
     * 挂单
     * @param transactionParam 挂单参数
     * @return
     */
    boolean add(TransactionParam transactionParam);
}
