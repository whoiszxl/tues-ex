package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.enums.BuySellEnum;
import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.enums.TransactionStatusEnum;
import com.whoiszxl.tues.common.utils.DateProvider;
import com.whoiszxl.tues.common.utils.IdWorker;
import com.whoiszxl.tues.member.dao.MemberWalletDao;
import com.whoiszxl.tues.member.entity.UmsMemberWallet;
import com.whoiszxl.tues.trade.dao.OmsOrderDao;
import com.whoiszxl.tues.trade.dao.OmsTransactionDao;
import com.whoiszxl.tues.trade.entity.OmsOrder;
import com.whoiszxl.tues.trade.entity.OmsTransaction;
import com.whoiszxl.tues.trade.service.MatchService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private DateProvider dateProvider;

    /**
     * TODO 使用数据库撮合，后续更改为Java内存撮合
     * @param transactionData 挂单信息
     */
    @Override
    public void matchOrder(OmsTransaction transactionData) {
        //查询其他人的挂单，并且交易方向是反向的记录
        List<OmsTransaction> otherTransactionList = transactionData.getType().equals(BuySellEnum.BUY.getValue()) ?
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

        if(ObjectUtils.isEmpty(otherTransactionList)) {
            return;
        }

        //剩余的交易量如果再撮合了一笔价格不对等的交易，需要把差价转换为相应的剩余量
        //多出来的钱需要再进行匹配，留在transaction表中，价差如1元，数量则需要乘以数量（surplusCount = surplusCount + 差价 * 交易的数量）
        //多出来的surplusCount还需要除以卖出的单价，最后公式如下（surplusCount = surplusCount + 差价 * 交易的数量 / price）
        int index = 0;
        BigDecimal allTransactionBalance = BigDecimal.ZERO;
        BigDecimal surplusCount = transactionData.getCurrentCount();

        while(surplusCount.compareTo(BigDecimal.ZERO) > 0 && index < otherTransactionList.size()) {
            OmsTransaction otherTransaction = otherTransactionList.get(index);
            BigDecimal transactionCount;

            //如果其他人的一笔挂单数量大于当前挂单需撮合的数量，则当前挂单全部数量被撮合成功，否则仅对当前挂单数递减
            if(otherTransaction.getCurrentCount().compareTo(surplusCount) > 0) {
                transactionCount = surplusCount;
                surplusCount = BigDecimal.ZERO;
            }else {
                transactionCount = otherTransaction.getCurrentCount();
                surplusCount = surplusCount.subtract(otherTransaction.getCurrentCount());
            }

            //累计交易方的交易总金额
            allTransactionBalance = allTransactionBalance.add(transactionCount.multiply(otherTransaction.getPrice()));

            //处理被交易方的数据
            this.handleTransaction(otherTransaction, transactionCount, null);
            index++;
        }

        //处理交易方的数据
        this.handleTransaction(transactionData, transactionData.getCurrentCount().subtract(surplusCount), allTransactionBalance);
        //TODO 增加coin的交易量

    }

    /**
     * 处理挂单，1.更新挂单数据 2.增加已成交订单数据 3.增减余额
     * @param rowData 需要处理的挂单对象
     * @param transactionCount 需要处理的数量
     * @param allTransactionBalance 是否是处理交易方数据   为null处理被交易方，有数据则处理交易方
     */
    public void handleTransaction(OmsTransaction rowData, BigDecimal transactionCount, BigDecimal allTransactionBalance) {
        //1. 更新挂单列表的数据（被交易方的记录）,减去交易的数量，并且如果交易数和剩余数量一样则关闭挂单
        Integer status = rowData.getCurrentCount().compareTo(transactionCount) == 0 ?
                TransactionStatusEnum.TRADE_CLOSE.getValue() : TransactionStatusEnum.TRADE_OPEN.getValue();
        transactionDao.changeCountAndStatus(transactionCount, status, rowData.getId());

        //2. 新增成交订单数据
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setId(idWorker.nextId());
        omsOrder.setMemberId(rowData.getMemberId());
        omsOrder.setTransactionId(rowData.getId().toString());
        omsOrder.setCoinId(rowData.getCoinId());
        omsOrder.setReplaceCoinId(rowData.getReplaceCoinId());
        omsOrder.setPrice(rowData.getPrice());
        omsOrder.setType(rowData.getType());
        omsOrder.setSuccessCount(transactionCount);
        omsOrder.setCreatedAt(dateProvider.now());
        omsOrder.setUpdatedAt(dateProvider.now());

        orderDao.save(omsOrder);

        //3. 增加买入余额的数量
        Integer addCoinId;
        BigDecimal addCoinCount;

        //如果是买，则增加的是交易对第一个币种的数量，数量就是买入数量
        if(rowData.getType().equals(BuySellEnum.BUY.getValue())) {
            addCoinId = rowData.getCoinId();
            addCoinCount = transactionCount;
        }else {
            //如果是卖，增加的是交易对第二个币种的数量，数量是 买入数量*单价
            addCoinId = rowData.getReplaceCoinId();
            addCoinCount = transactionCount.multiply(rowData.getPrice());
        }

        if(allTransactionBalance != null) {
            addCoinCount = allTransactionBalance;
        }

        // 查询member_wallet表中记录是否存在，存在更新，不存在则新增
        UmsMemberWallet userBalance = memberWalletDao.findByMemberIdAndCoinId(rowData.getMemberId(), addCoinId);
        if(userBalance == null) {
            //新增
            UmsMemberWallet zxlUserBalance = new UmsMemberWallet();
            zxlUserBalance.setId(idWorker.nextId());
            zxlUserBalance.setMemberId(rowData.getMemberId());
            zxlUserBalance.setCoinId(addCoinId);
            zxlUserBalance.setCoinName(addCoinId.toString());
            zxlUserBalance.setLockBalance(BigDecimal.ZERO);
            zxlUserBalance.setUsableBalance(addCoinCount);
            zxlUserBalance.setStatus(SwitchStatusEnum.STATUS_OPEN.getStatusCode());
            zxlUserBalance.setCreatedAt(dateProvider.now());
            zxlUserBalance.setUpdatedAt(dateProvider.now());
            memberWalletDao.saveMemberWallet(zxlUserBalance);
        }else {
            //更新
            memberWalletDao.addBalance(rowData.getMemberId(), addCoinId, addCoinCount);
        }

        //4. 减少卖出的余额数量
        Integer subCoinId;
        BigDecimal subCoinCount;
        if(rowData.getType().equals(BuySellEnum.BUY.getValue())) {
            subCoinId = rowData.getReplaceCoinId();
            subCoinCount = transactionCount.multiply(rowData.getPrice());
        }else {
            subCoinId = rowData.getCoinId();
            subCoinCount = transactionCount;
        }
        memberWalletDao.subLockBalance(rowData.getMemberId(), subCoinId, subCoinCount);
    }
}
