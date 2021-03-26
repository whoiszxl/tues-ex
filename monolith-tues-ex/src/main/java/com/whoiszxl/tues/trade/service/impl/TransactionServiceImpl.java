package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.enums.BuySellEnum;
import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.enums.TransactionStatusEnum;
import com.whoiszxl.tues.common.exception.ExceptionCatcher;
import com.whoiszxl.tues.common.utils.DateProvider;
import com.whoiszxl.tues.common.utils.IdWorker;
import com.whoiszxl.tues.member.dao.MemberWalletDao;
import com.whoiszxl.tues.member.entity.UmsMemberWallet;
import com.whoiszxl.tues.trade.dao.OmsTransactionDao;
import com.whoiszxl.tues.trade.entity.OmsTransaction;
import com.whoiszxl.tues.trade.entity.dto.TransactionParamCheckDTO;
import com.whoiszxl.tues.trade.entity.param.TransactionParam;
import com.whoiszxl.tues.trade.service.MatchService;
import com.whoiszxl.tues.trade.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 挂单服务实现
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private MemberWalletDao memberWalletDao;

    @Autowired
    private OmsTransactionDao transactionDao;

    @Autowired
    private MatchService matchService;

    @Autowired
    private DateProvider dateProvider;

    @Autowired
    private IdWorker idWorker;

    @Override
    public boolean add(TransactionParam transactionParam) {
        Long memberId = transactionParam.getMemberId();

        //通过买卖方向获取需要交易的币种ID和交易的金额（数量）
        TransactionParamCheckDTO checkParam = this.getCheckParam(transactionParam);
        Integer checkCoinId = checkParam.getCheckCoinId();
        BigDecimal transactionBalance = checkParam.getTransactionBalance();
        Integer buyOrSell = checkParam.getBuyOrSell();

        //校验余额是否充足
        UmsMemberWallet memberWallet = memberWalletDao.findByMemberIdAndCoinId(memberId, checkCoinId);
        this.checkBalanceVaild(transactionBalance, memberWallet);

        //锁定余额: 减去usable_balance,加上lock_balance
        memberWalletDao.lockBalance(memberId, checkCoinId, transactionBalance);

        //将数据添加到挂单表
        OmsTransaction transaction = new OmsTransaction();
        transaction.setId(idWorker.nextId());
        transaction.setMemberId(memberId);
        transaction.setCoinId(transactionParam.getCoinId());
        transaction.setReplaceCoinId(transactionParam.getReplaceCoinId());
        transaction.setPrice(transactionParam.getPrice());
        transaction.setTotalCount(transactionParam.getCount());
        transaction.setCurrentCount(transactionParam.getCount());
        transaction.setType(buyOrSell);
        transaction.setStatus(TransactionStatusEnum.TRADE_OPEN.getValue());
        transaction.setCreatedAt(dateProvider.now());
        transaction.setUpdatedAt(dateProvider.now());
        transactionDao.save(transaction);

        return false;
    }



    /**
     * 传入参数获取到需要校验的币种ID和操作金额
     * @param transactionParam 检验参数
     * @return TransactionParamCheckDTO 返回结果
     */
    private TransactionParamCheckDTO getCheckParam(TransactionParam transactionParam) {
        Integer checkCoinId = null;
        BigDecimal transactionBalance = null;
        Integer buyOrSell = 1;

        //如果交易方向是买，则需要用第二个币种换第一个
        //如果交易对为 ETH/USDT，方向是买，则ETH是被买的币种，需要用USDT换ETH。
        //购买价格如果为5USDT，数量为2,则是购买2个ETH需要10个USDT。
        if(transactionParam.getType().equals(BuySellEnum.BUY.getValue())) {
            checkCoinId = transactionParam.getReplaceCoinId();
            transactionBalance = transactionParam.getPrice().multiply(transactionParam.getCount());
            buyOrSell = BuySellEnum.BUY.getValue();
        }

        //如果交易对为 ETH/USDT，方向是卖，则ETH是被卖的币种，需要进行余额校验的。
        //交易支出的金额就是交易数量，2个ETH。
        if(transactionParam.getType().equals(BuySellEnum.SELL.getValue())) {
            checkCoinId = transactionParam.getCoinId();
            transactionBalance = transactionParam.getCount();
            buyOrSell = BuySellEnum.SELL.getValue();
        }

        return TransactionParamCheckDTO.builder()
                .checkCoinId(checkCoinId)
                .transactionBalance(transactionBalance)
                .buyOrSell(buyOrSell)
                .build();
    }

    /**
     * 校验输入的金额是否合法
     * @param transactionBalance 交易金额
     * @param memberWallet 用户资产信息
     */
    private void checkBalanceVaild(BigDecimal transactionBalance, UmsMemberWallet memberWallet) {
        if(memberWallet == null
                || SwitchStatusEnum.STATUS_CLOSE.getStatusCode().equals(memberWallet.getStatus())
                || memberWallet.getUsableBalance().compareTo(transactionBalance) < 0) {
            ExceptionCatcher.catchValidateEx(Result.buildError("余额不足"));
        }
    }
}
