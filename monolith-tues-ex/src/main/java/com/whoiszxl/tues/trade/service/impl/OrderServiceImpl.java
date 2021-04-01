package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.enums.BuySellEnum;
import com.whoiszxl.tues.common.enums.OrderStatusEnum;
import com.whoiszxl.tues.common.enums.OrderTypeEnum;
import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.exception.ExceptionCatcher;
import com.whoiszxl.tues.common.mq.MessageTypeConstants;
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
import com.whoiszxl.tues.trade.entity.dto.OmsPairDTO;
import com.whoiszxl.tues.trade.entity.dto.OrderParamCheckDTO;
import com.whoiszxl.tues.trade.entity.param.OrderParam;
import com.whoiszxl.tues.trade.service.MatchService;
import com.whoiszxl.tues.trade.service.OrderService;
import com.whoiszxl.tues.trade.service.PairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 挂单服务实现
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MemberWalletDao memberWalletDao;

    @Autowired
    private OmsOrderDao orderDao;

    @Autowired
    private OmsDealDao dealDao;

    @Autowired
    private PairService pairService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private DateProvider dateProvider;

    @Autowired
    private IdWorker idWorker;

    @Override
    //@Transactional(rollbackFor = Exception.class)
    public boolean add(OrderParam orderParam) {
        //获取交易对信息
        List<OmsPairDTO> pairDTOList = pairService.pairList();
        Optional<OmsPairDTO> pairDTO = pairDTOList.stream().filter(item -> item.getId().equals(orderParam.getPairId())).findFirst();
        OmsPairDTO omsPairDTO = pairDTO.get();

        Long memberId = orderParam.getMemberId();

        //通过买卖方向获取需要交易的币种ID和交易的金额（数量）
        OrderParamCheckDTO checkParam = this.getCheckParam(orderParam, omsPairDTO);
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
        order.setPairName(omsPairDTO.getPairName());
        order.setCoinId(omsPairDTO.getCoinId());
        order.setReplaceCoinId(omsPairDTO.getReplaceCoinId());
        order.setPrice(orderParam.getPrice());
        order.setTotalCount(orderParam.getCount());
        order.setCurrentCount(orderParam.getCount());
        order.setDirection(buyOrSell);
        order.setType(OrderTypeEnum.LIMIT.getValue());
        order.setStatus(OrderStatusEnum.TRADE_OPEN.getValue());
        order.setCreatedAt(dateProvider.now());
        order.setUpdatedAt(dateProvider.now());
        OmsOrder result = orderDao.save(order);

        matchService.send(result, MessageTypeConstants.NEW_ORDER);

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
    private OrderParamCheckDTO getCheckParam(OrderParam orderParam, OmsPairDTO omsPairDTO) {
        Integer checkCoinId = null;
        BigDecimal orderBalance = null;
        Integer buyOrSell = 1;

        //如果交易方向是买，则需要用第二个币种换第一个
        //如果交易对为 ETH/USDT，方向是买，则ETH是被买的币种，需要用USDT换ETH。
        //购买价格如果为5USDT，数量为2,则是购买2个ETH需要10个USDT。
        if(orderParam.getType().equals(BuySellEnum.BUY.getValue())) {
            checkCoinId = omsPairDTO.getReplaceCoinId();
            orderBalance = orderParam.getPrice().multiply(orderParam.getCount());
            buyOrSell = BuySellEnum.BUY.getValue();
        }

        //如果交易对为 ETH/USDT，方向是卖，则ETH是被卖的币种，需要进行余额校验的。
        //交易支出的金额就是交易数量，2个ETH。
        if(orderParam.getType().equals(BuySellEnum.SELL.getValue())) {
            checkCoinId = omsPairDTO.getCoinId();
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

    @Override
    public void handleOrderSuccess(Long orderId, BigDecimal volume, BigDecimal turnover) {
        //订单交易成功状态更新
        OmsOrder order = orderDao.findById(orderId);
        if(!OrderStatusEnum.TRADE_OPEN.getValue().equals(order.getStatus())) {
            return;
        }
        order.setTurnover(turnover);
        order.setCurrentCount(BigDecimal.ZERO);
        order.setStatus(OrderStatusEnum.TRADE_CLOSE.getValue());
        order.setCompletedAt(dateProvider.now());
        order.setUpdatedAt(dateProvider.now());
        orderDao.updateOrder(order);


        refundBalance(order, volume, turnover);


    }


    /**
     * -- 退差额 多退少补 --
     * 假设当前有两个卖单桶：
     * 卖单桶one -> 卖价20元 -> 单数5单,数量分别为：[10,20,30,40,50]
     * 卖单桶two -> 卖价30元 -> 单数5单,数量分别为：[1,2,3,4,5]
     * 我发起一个买单，买价25元，买60的量，那么这笔买单就会去卖单桶one里进行匹配。(价格优先，时间优先原则)
     * 会从卖单桶里吃掉[10,20,30]这三笔单，然后实行（多退少补）原则，退 "(买价-卖价)*量" 元, 则为: (25-20)*60=300元
     * @param order 订单信息
     * @param volume 总量
     * @param turnover 总额
     **/
    private void refundBalance(OmsOrder order, BigDecimal volume, BigDecimal turnover) {
        BigDecimal actualBalance, freezeBalance;
        //如果是买单，冻结的数量则是当前单的委托总数*委托价格，实际数量则是撮合返回的交易总额
        if(order.getDirection().equals(BuySellEnum.BUY.getValue())) {
            freezeBalance = order.getTotalCount().multiply(order.getPrice());
            actualBalance = turnover;
        }else {
            freezeBalance = order.getTotalCount();
            actualBalance = volume;
        }

        // BTC/USDT 拿到需要操作的币种ID，如果是买，则操作USDT, 卖则操作BTC
        Integer coinId = order.getDirection().equals(BuySellEnum.SELL.getValue()) ? order.getCoinId() : order.getReplaceCoinId();
        UmsMemberWallet memberWallet = memberWalletDao.findByMemberIdAndCoinId(order.getMemberId(), coinId);

        //将此笔订单冻结的金额减去实际成交额
        BigDecimal refundBalance = freezeBalance.subtract(actualBalance);
        if(refundBalance.compareTo(BigDecimal.ZERO) > 0) {
            memberWalletDao.unlockBalance(order.getMemberId(), order.getCoinId(), refundBalance);
        }
    }
}
