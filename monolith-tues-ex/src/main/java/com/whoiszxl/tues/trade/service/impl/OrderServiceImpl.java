package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.enums.BuySellEnum;
import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.enums.OrderStatusEnum;
import com.whoiszxl.tues.common.exception.ExceptionCatcher;
import com.whoiszxl.tues.common.mq.MessageTypeEnum;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.common.utils.DateProvider;
import com.whoiszxl.tues.common.utils.IdWorker;
import com.whoiszxl.tues.member.dao.MemberWalletDao;
import com.whoiszxl.tues.member.entity.UmsMemberWallet;
import com.whoiszxl.tues.trade.dao.OmsDealDao;
import com.whoiszxl.tues.trade.dao.OmsOrderDao;
import com.whoiszxl.tues.trade.entity.OmsDeal;
import com.whoiszxl.tues.trade.entity.OmsOrder;
import com.whoiszxl.tues.trade.entity.dto.OmsDealDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsOrderDTO;
import com.whoiszxl.tues.trade.entity.dto.OrderParamCheckDTO;
import com.whoiszxl.tues.trade.entity.param.OrderParam;
import com.whoiszxl.tues.trade.service.MatchService;
import com.whoiszxl.tues.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 挂单服务实现
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MemberWalletDao memberWalletDao;

    @Autowired
    private OmsOrderDao orderDao;

    @Autowired
    private OmsDealDao dealDao;

    @Autowired
    private MatchService matchService;

    @Autowired
    private DateProvider dateProvider;

    @Autowired
    private IdWorker idWorker;

    @Override
    //@Transactional(rollbackFor = Exception.class)
    public boolean add(OrderParam orderParam) {
        Long memberId = orderParam.getMemberId();

        //通过买卖方向获取需要交易的币种ID和交易的金额（数量）
        OrderParamCheckDTO checkParam = this.getCheckParam(orderParam);
        Integer checkCoinId = checkParam.getCheckCoinId();
        BigDecimal orderBalance = checkParam.getOrderBalance();
        Integer buyOrSell = checkParam.getBuyOrSell();

        //校验余额是否充足
        UmsMemberWallet memberWallet = memberWalletDao.findByMemberIdAndCoinId(memberId, checkCoinId);
        this.checkBalanceValid(orderBalance, memberWallet);

        //锁定余额: 减去usable_balance,加上lock_balance
        memberWalletDao.lockBalance(memberId, checkCoinId, orderBalance);

        //将数据添加到挂单表
        OmsOrder order = new OmsOrder();
        order.setId(idWorker.nextId());
        order.setMemberId(memberId);
        order.setCoinId(orderParam.getCoinId());
        order.setReplaceCoinId(orderParam.getReplaceCoinId());
        order.setPrice(orderParam.getPrice());
        order.setTotalCount(orderParam.getCount());
        order.setCurrentCount(orderParam.getCount());
        order.setDirection(buyOrSell);
        order.setStatus(OrderStatusEnum.TRADE_OPEN.getValue());
        order.setCreatedAt(dateProvider.now());
        order.setUpdatedAt(dateProvider.now());
        OmsOrder result = orderDao.save(order);

        matchService.send(result, MessageTypeEnum.NEW_ORDER);

        return true;
    }

    @Override
    public List<OmsOrderDTO> listOrder(Long memberId) {
        List<OmsOrder> orderList = orderDao.findAllByMemberIdOrderByIdDesc(memberId);
        return BeanCopierUtils.copyListProperties(orderList, OmsOrderDTO::new);
    }

    @Override
    public List<OmsDealDTO> listDeal(Long memberId, Long orderId) {
        List<OmsDeal> dealList = dealDao.findAllByMemberIdAndOrderIdOrderByIdDesc(memberId, orderId);
        return BeanCopierUtils.copyListProperties(dealList, OmsDealDTO::new);
    }

    /**
     * 传入参数获取到需要校验的币种ID和操作金额
     * @param orderParam 检验参数
     * @return OrderParamCheckDTO 返回结果
     */
    private OrderParamCheckDTO getCheckParam(OrderParam orderParam) {
        Integer checkCoinId = null;
        BigDecimal orderBalance = null;
        Integer buyOrSell = 1;

        //如果交易方向是买，则需要用第二个币种换第一个
        //如果交易对为 ETH/USDT，方向是买，则ETH是被买的币种，需要用USDT换ETH。
        //购买价格如果为5USDT，数量为2,则是购买2个ETH需要10个USDT。
        if(orderParam.getType().equals(BuySellEnum.BUY.getValue())) {
            checkCoinId = orderParam.getReplaceCoinId();
            orderBalance = orderParam.getPrice().multiply(orderParam.getCount());
            buyOrSell = BuySellEnum.BUY.getValue();
        }

        //如果交易对为 ETH/USDT，方向是卖，则ETH是被卖的币种，需要进行余额校验的。
        //交易支出的金额就是交易数量，2个ETH。
        if(orderParam.getType().equals(BuySellEnum.SELL.getValue())) {
            checkCoinId = orderParam.getCoinId();
            orderBalance = orderParam.getCount();
            buyOrSell = BuySellEnum.SELL.getValue();
        }

        return OrderParamCheckDTO.builder()
                .checkCoinId(checkCoinId)
                .orderBalance(orderBalance)
                .buyOrSell(buyOrSell)
                .build();
    }

    /**
     * 校验输入的金额是否合法
     * @param orderBalance 交易金额
     * @param memberWallet 用户资产信息
     */
    private void checkBalanceValid(BigDecimal orderBalance, UmsMemberWallet memberWallet) {
        if(memberWallet == null
                || SwitchStatusEnum.STATUS_CLOSE.getStatusCode().equals(memberWallet.getStatus())
                || memberWallet.getUsableBalance().compareTo(orderBalance) < 0) {
            ExceptionCatcher.catchValidateEx(Result.buildError("余额不足"));
        }
    }
}
