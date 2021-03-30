package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.enums.BuySellEnum;
import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.enums.OrderStatusEnum;
import com.whoiszxl.tues.common.utils.DateProvider;
import com.whoiszxl.tues.common.utils.IdWorker;
import com.whoiszxl.tues.member.dao.MemberWalletDao;
import com.whoiszxl.tues.member.entity.UmsMemberWallet;
import com.whoiszxl.tues.trade.dao.OmsDealDao;
import com.whoiszxl.tues.trade.dao.OmsOrderDao;
import com.whoiszxl.tues.trade.entity.OmsDeal;
import com.whoiszxl.tues.trade.entity.OmsOrder;
import com.whoiszxl.tues.trade.service.MatchService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * 撮合订单服务接口实现
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
//@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private OmsOrderDao orderDao;

    @Autowired
    private OmsDealDao dealDao;

    @Autowired
    private MemberWalletDao memberWalletDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private DateProvider dateProvider;

    /**
     * TODO 使用数据库撮合，后续更改为Java内存撮合
     * @param order 挂单信息
     */
    @Override
    public void send(OmsOrder order, String messageType) {
        //查询其他人的挂单，并且交易方向是反向的记录
        List<OmsOrder> otherOrderList = order.getDirection().equals(BuySellEnum.BUY.getValue()) ?
                orderDao.getBuyMatchOrderList(order.getDirection(),
                        order.getMemberId(),
                        order.getCoinId(),
                        order.getReplaceCoinId(),
                        order.getPrice())
                :
                orderDao.getSellMatchOrderList(order.getDirection(),
                        order.getMemberId(),
                        order.getCoinId(),
                        order.getReplaceCoinId(),
                        order.getPrice());

        if(ObjectUtils.isEmpty(otherOrderList)) {
            return;
        }

        //剩余的交易量如果再撮合了一笔价格不对等的交易，需要把差价转换为相应的剩余量
        //多出来的钱需要再进行匹配，留在order表中，价差如1元，数量则需要乘以数量（surplusCount = surplusCount + 差价 * 交易的数量）
        //多出来的surplusCount还需要除以卖出的单价，最后公式如下（surplusCount = surplusCount + 差价 * 交易的数量 / price）
        int index = 0;
        BigDecimal allOrderBalance = BigDecimal.ZERO;
        BigDecimal surplusCount = order.getCurrentCount();

        while(surplusCount.compareTo(BigDecimal.ZERO) > 0 && index < otherOrderList.size()) {
            OmsOrder otherOrder = otherOrderList.get(index);
            BigDecimal orderCount;

            //如果其他人的一笔挂单数量大于当前挂单需撮合的数量，则当前挂单全部数量被撮合成功，否则仅对当前挂单数递减
            if(otherOrder.getCurrentCount().compareTo(surplusCount) > 0) {
                orderCount = surplusCount;
                surplusCount = BigDecimal.ZERO;
            }else {
                orderCount = otherOrder.getCurrentCount();
                surplusCount = surplusCount.subtract(otherOrder.getCurrentCount());
            }

            //累计交易方的交易总金额
            allOrderBalance = allOrderBalance.add(orderCount.multiply(otherOrder.getPrice()));

            //处理被交易方的数据
            this.handleOrder(otherOrder, orderCount, null);
            index++;
        }

        //处理交易方的数据
        this.handleOrder(order, order.getCurrentCount().subtract(surplusCount), allOrderBalance);
        //TODO 增加coin的交易量

    }

    /**
     * 处理挂单，1.更新挂单数据 2.增加已成交订单数据 3.增减余额
     * @param rowData 需要处理的挂单对象
     * @param orderCount 需要处理的数量
     * @param allOrderBalance 是否是处理交易方数据   为null处理被交易方，有数据则处理交易方
     */
    public void handleOrder(OmsOrder rowData, BigDecimal orderCount, BigDecimal allOrderBalance) {
        //1. 更新挂单列表的数据（被交易方的记录）,减去交易的数量，并且如果交易数和剩余数量一样则关闭挂单
        Integer status = rowData.getCurrentCount().compareTo(orderCount) == 0 ?
                OrderStatusEnum.TRADE_CLOSE.getValue() : OrderStatusEnum.TRADE_OPEN.getValue();
        orderDao.changeCountAndStatus(orderCount, status, rowData.getId());

        //2. 新增成交订单数据
        OmsDeal omsDeal = new OmsDeal();
        omsDeal.setId(idWorker.nextId());
        omsDeal.setMemberId(rowData.getMemberId());
        omsDeal.setOrderId(rowData.getId());
        omsDeal.setCoinId(rowData.getCoinId());
        omsDeal.setReplaceCoinId(rowData.getReplaceCoinId());
        omsDeal.setPrice(rowData.getPrice());
        omsDeal.setType(rowData.getDirection());
        omsDeal.setSuccessCount(orderCount);
        omsDeal.setCreatedAt(dateProvider.now());
        omsDeal.setUpdatedAt(dateProvider.now());

        dealDao.save(omsDeal);

        //3. 增加买入余额的数量
        Integer addCoinId;
        BigDecimal addCoinCount;

        //如果是买，则增加的是交易对第一个币种的数量，数量就是买入数量
        if(rowData.getDirection().equals(BuySellEnum.BUY.getValue())) {
            addCoinId = rowData.getCoinId();
            addCoinCount = orderCount;
        }else {
            //如果是卖，增加的是交易对第二个币种的数量，数量是 买入数量*单价
            addCoinId = rowData.getReplaceCoinId();
            addCoinCount = orderCount.multiply(rowData.getPrice());
        }

        if(allOrderBalance != null) {
            addCoinCount = allOrderBalance;
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
        if(rowData.getDirection().equals(BuySellEnum.BUY.getValue())) {
            subCoinId = rowData.getReplaceCoinId();
            subCoinCount = orderCount.multiply(rowData.getPrice());
        }else {
            subCoinId = rowData.getCoinId();
            subCoinCount = orderCount;
        }
        memberWalletDao.subLockBalance(rowData.getMemberId(), subCoinId, subCoinCount);
    }
}
