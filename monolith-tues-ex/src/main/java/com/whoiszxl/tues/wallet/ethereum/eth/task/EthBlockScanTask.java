package com.whoiszxl.tues.wallet.ethereum.eth.task;

import com.whoiszxl.tues.common.constants.CoinNameConstants;
import com.whoiszxl.tues.common.enums.UpchainStatusEnum;
import com.whoiszxl.tues.common.utils.AssertUtils;
import com.whoiszxl.tues.common.utils.DateProvider;
import com.whoiszxl.tues.common.utils.IdWorker;
import com.whoiszxl.tues.member.entity.dto.UmsMemberAddressDTO;
import com.whoiszxl.tues.member.service.MemberAddressService;
import com.whoiszxl.tues.member.service.MemberWalletService;
import com.whoiszxl.tues.trade.entity.OmsDeposit;
import com.whoiszxl.tues.trade.entity.OmsHeight;
import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsDepositDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsHeightDTO;
import com.whoiszxl.tues.trade.service.CoinService;
import com.whoiszxl.tues.trade.service.DepositService;
import com.whoiszxl.tues.wallet.ethereum.core.service.EthereumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 以太币扫描服务，定时任务执行
 * @author whoiszxl
 * @date 2021/3/23
 */
@Slf4j
@Component
public class EthBlockScanTask {

    @Autowired
    private EthereumService ethereumService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private DateProvider dateProvider;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MemberWalletService memberWalletService;

    private final String coinName = CoinNameConstants.ETH;

    /**
     * 扫描链上的交易是否和数据库中的充值单是否匹配，如果匹配则修改对应状态。
     * 在最近的300个区块的出块时间一般平均为15秒。
     * 定时任务使用10秒间隔（10 * 1000）。
     * https://txstreet.com/
     */
    @Scheduled(fixedDelay = 10 * 1000)
    public void scanOrder() {
        //获取当前货币的配置信息
        OmsCoinDTO coinInfo = coinService.findCoinByName(coinName);

        //获取到当前网络区块高度
        Long networkBlockHeight = ethereumService.getBlockchainHeight();

        //获取到系统扫描到的区块记录，不存在则创建并抛出异常
        OmsHeightDTO heightObj = depositService.getOrCreateCurrentHeight(coinName, coinInfo, networkBlockHeight);

        Long currentHeight = heightObj.getHeight();

        //相隔1个区块不进行扫描
        AssertUtils.isFalse(networkBlockHeight - currentHeight <= 1, "不存在需要扫描的区块");

        //扫描区块中的交易
        for(long i = currentHeight + 1; i <= networkBlockHeight; i++) {
            //通过区块高度获取到区块信息， 并从区块信息中拿到交易信息列表
            EthBlock.Block block = ethereumService.getBlockByNumber(i);
            List<EthBlock.TransactionResult> transactions = block.getTransactions();

            //遍历所有交易
            for (EthBlock.TransactionResult transactionResult : transactions) {
                EthBlock.TransactionObject transactionObject = (EthBlock.TransactionObject) transactionResult;
                Transaction transaction = transactionObject.get();

                if(StringUtils.isEmpty(transaction.getTo())) {
                    log.info("交易{}不存在toAddress", transaction.getHash());
                    continue;
                }

                BigDecimal amount = Convert.fromWei(transaction.getValue().toString(), Convert.Unit.ETHER);

                //从用户地址库中查询是否存在地址
                UmsMemberAddressDTO umsMemberAddressDTO = memberAddressService.findByDepositAddressAndCoinId(transaction.getTo(), coinInfo.getId());
                if(ObjectUtils.isEmpty(umsMemberAddressDTO)) {
                    log.info("地址不在库中：{}", transaction.getTo());
                    continue;
                }
                OmsDeposit deposit = new OmsDeposit();
                deposit.setId(idWorker.nextId());
                deposit.setCoinId(coinInfo.getId());
                deposit.setCoinName(coinInfo.getCoinName());
                deposit.setDepositActual(amount);
                deposit.setMemberId(umsMemberAddressDTO.getMemberId());
                deposit.setFromAddress(transaction.getFrom());
                deposit.setToAddress(transaction.getTo());
                deposit.setTxHash(transaction.getHash());
                deposit.setCurrentConfirm(transaction.getBlockNumber().subtract(BigInteger.valueOf(i)).intValue());
                deposit.setHeight(transaction.getBlockNumber().longValue());
                deposit.setUpchainAt(dateProvider.longToLocalDateTime(block.getTimestamp().longValue()));

                deposit.setUpdatedAt(dateProvider.now());
                deposit.setCreatedAt(dateProvider.now());
                if(i - block.getNumber().intValue() >= coinInfo.getConfirms()) {
                    deposit.setUpchainStatus(UpchainStatusEnum.SUCCESS.getCode());
                    deposit.setUpchainSuccessAt(dateProvider.longToLocalDateTime(block.getTimestamp().longValue()));
                }else {
                    deposit.setUpchainStatus(UpchainStatusEnum.WAITING_CONFIRM.getCode());
                }
                depositService.saveRecharge(deposit);
            }

        }

        //更新区块高度
        heightObj.setHeight(networkBlockHeight);
        heightObj.setUpdatedAt(dateProvider.now());
        depositService.saveCurrentHeight(heightObj.clone(OmsHeight.class));
    }



    /**
     * 确认交易，将数据库中状态为待确认的充值单再次去链上查询是否确认数超过了配置确认数。
     * 在最近的300个区块的出块时间一般平均为15秒。
     * 定时任务使用15秒间隔（15 * 1000）。
     * https://txstreet.com/
     */
    @Scheduled(fixedDelay = 10 * 1000)
    public void confirmTx() {
        //0. 获取当前货币的配置信息
        //获取当前货币的配置信息
        OmsCoinDTO coinInfo = coinService.findCoinByName(coinName);
        AssertUtils.isNotNull(coinInfo, "数据库未配置货币信息：" + coinName);

        //1. 获取当前网络的区块高度
        Long currentHeight = ethereumService.getBlockchainHeight();

        //2. 查询到所有待确认的充值单
        List<OmsDepositDTO> waitConfirmDeposit = depositService.getWaitConfirmDeposit(coinName);
        AssertUtils.isNotNull(waitConfirmDeposit, "不存在待确认的充值单");

        //3. 遍历库中交易进行判断是否成功
        for (OmsDepositDTO depositDTO : waitConfirmDeposit) {
            Transaction transaction = ethereumService.getTransactionByHash(depositDTO.getTxHash());

            //如果链上交易确认数大于等于配置的确认数，则更新充值单为成功并更新上链成功时间，否则只更新当前确认数。
            if(currentHeight - transaction.getBlockNumber().longValue()  >= coinInfo.getConfirms()) {
                depositDTO.setUpchainStatus(UpchainStatusEnum.SUCCESS.getCode());
                depositDTO.setUpchainSuccessAt(dateProvider.now());
            }
            depositDTO.setCurrentConfirm((int) (currentHeight - transaction.getBlockNumber().longValue()));
            depositDTO.setUpdatedAt(dateProvider.now());

            depositService.updateRecharge(depositDTO.clone(OmsDeposit.class));

            //给用户账户增加余额
            memberWalletService.addBalance(depositDTO.getMemberId(), depositDTO.getCoinId(), depositDTO.getCoinName(), depositDTO.getDepositActual());
        }
    }


}
