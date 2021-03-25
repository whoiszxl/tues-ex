package com.whoiszxl.tues.wallet.btc.task;

import com.whoiszxl.tues.common.constants.CoinNameConstants;
import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.enums.UpchainStatusEnum;
import com.whoiszxl.tues.common.utils.AssertUtils;
import com.whoiszxl.tues.common.utils.DateProvider;
import com.whoiszxl.tues.common.utils.IdWorker;
import com.whoiszxl.tues.member.entity.dto.UmsMemberAddressDTO;
import com.whoiszxl.tues.member.service.MemberAddressService;
import com.whoiszxl.tues.trade.entity.OmsDeposit;
import com.whoiszxl.tues.trade.entity.OmsHeight;
import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsDepositDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsHeightDTO;
import com.whoiszxl.tues.trade.service.CoinService;
import com.whoiszxl.tues.trade.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * BTC区块扫描任务
 * 做充值与确认充值
 * @author whoiszxl
 * @date 2021/3/24
 */
@Slf4j
@Component
public class BtcBlockScanTask {

    @Autowired
    private DepositService depositService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private DateProvider dateProvider;

    @Autowired
    private BitcoindRpcClient bitcoinClient;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MemberAddressService memberAddressService;

    private String coinName = CoinNameConstants.BTC;

    /**
     * 扫描链上的交易是否和数据库中的充值单是否匹配，如果匹配则修改对应状态。
     * 在最近的250个区块的出块时间一般平均为10分钟，所以定时任务运行的时间可以稍微拉长一些，降低服务器与节点的压力。
     * 测试链使用4秒间隔，主网使用5分钟间隔（300 * 1000）。
     * https://txstreet.com/
     */
    @Scheduled(fixedDelay = 4 * 1000)
    public void scanBlock() {
        //获取当前货币的配置信息
        OmsCoinDTO coinInfo = coinService.findCoinByName(coinName);

        //获取到当前网络区块高度
        Long networkBlockHeight = Long.parseLong(bitcoinClient.getBlockCount() + "");

        //获取到系统扫描到的区块记录，不存在则创建并抛出异常
        OmsHeightDTO heightObj = depositService.getOrCreateCurrentHeight(coinName, coinInfo, networkBlockHeight);
        Long currentHeight = heightObj.getHeight();

        //相隔1个区块不进行扫描
        AssertUtils.isFalse(networkBlockHeight.longValue() - currentHeight <= 1, "区块相隔1个以内,不需要扫描");

        //扫描区块中的交易
        for(Long i = currentHeight + 1; i <= networkBlockHeight; i++) {
            //通过区块高度拿到区块Hash，再通过区块Hash拿到区块对象，再从区块对象中拿到交易ID集合
            String blockHash = bitcoinClient.getBlockHash(i.intValue());
            BitcoindRpcClient.Block block = bitcoinClient.getBlock(blockHash);
            List<String> txs = block.tx();

            //遍历区块中的所有交易，判断是否在咱们的数据库中
            for (String txId : txs) {
                //通过交易ID获取到交易对象，从交易对象中拿到交易输出，交易输出就是交易的收款方信息。
                BitcoindRpcClient.RawTransaction transaction = bitcoinClient.getRawTransaction(txId);
                List<BitcoindRpcClient.RawTransaction.Out> outs = transaction.vOut();

                //判断交易输出集是否有效
                if(CollectionUtils.isEmpty(outs)) {
                    continue;
                }

                //遍历交易输出集
                for(BitcoindRpcClient.RawTransaction.Out out : outs) {
                    //判断公钥脚本是否有效
                    if(out.scriptPubKey() == null) {
                        continue;
                    }

                    //拿到地址在数据库判断记录是否存在
                    if(CollectionUtils.isEmpty(out.scriptPubKey().addresses())) {
                        continue;
                    }
                    String address = out.scriptPubKey().addresses().get(0);
                    BigDecimal amount = out.value();

                    //从用户地址库中查询是否存在地址
                    UmsMemberAddressDTO umsMemberAddressDTO = memberAddressService.findByDepositAddressAndCoinId(address, coinInfo.getId());
                    if(ObjectUtils.isEmpty(umsMemberAddressDTO)) {
                        log.info("地址不在库中：{}", address);
                        continue;
                    }

                    OmsDeposit deposit = new OmsDeposit();

                    //更新recharge表
                    deposit.setId(idWorker.nextId());
                    deposit.setCoinId(coinInfo.getId());
                    deposit.setCoinName(coinInfo.getCoinName());
                    deposit.setDepositActual(amount);
                    deposit.setMemberId(umsMemberAddressDTO.getMemberId());
                    deposit.setFromAddress("");
                    deposit.setToAddress(address);
                    deposit.setTxHash(txId);
                    deposit.setCurrentConfirm(block.confirmations());
                    deposit.setHeight(Long.parseLong(block.height() + ""));
                    deposit.setUpchainAt(dateProvider.dateToLocalDateTime(block.time()));

                    deposit.setUpdatedAt(dateProvider.now());
                    deposit.setCreatedAt(dateProvider.now());

                    if(block.confirmations() >= coinInfo.getConfirms()) {
                        deposit.setUpchainSuccessAt(dateProvider.dateToLocalDateTime(block.time()));
                        deposit.setUpchainStatus(UpchainStatusEnum.SUCCESS.getCode());
                    }else {
                        deposit.setUpchainStatus(UpchainStatusEnum.WAITING_CONFIRM.getCode());
                    }
                    depositService.saveRecharge(deposit);
                }

            }

        }

        //更新区块高度
        heightObj.setHeight(networkBlockHeight);
        heightObj.setUpdatedAt(dateProvider.now());
        depositService.saveCurrentHeight(heightObj.clone(OmsHeight.class));
    }



    /**
     * 确认交易，将数据库中状态为待确认的充值单再次去链上查询是否确认数超过了配置确认数。
     * 在最近的250个区块的出块时间一般平均为10分钟，所以定时任务运行的时间可以稍微拉长一些，降低服务器与节点的压力。
     * 测试链使用5秒间隔，主网则使用10分钟间隔（600 * 1000）。
     * https://txstreet.com/
     */
    @Scheduled(fixedDelay = 5000)
    public void confirmTx() {
        //0. 获取当前货币的配置信息
        OmsCoinDTO coinInfo = coinService.findCoinByName(coinName);

        //1. 查询到所有待确认的充值单
        List<OmsDepositDTO> waitConfirmDeposit = depositService.getWaitConfirmDeposit(coinInfo.getCoinName());
        AssertUtils.isNotNull(waitConfirmDeposit, "不存在待确认的充值单");

        //2. 遍历库中交易进行判断是否成功
        for (OmsDepositDTO depositDTO : waitConfirmDeposit) {
            BitcoindRpcClient.RawTransaction transaction = bitcoinClient.getRawTransaction(depositDTO.getTxHash());

            //如果链上交易确认数大于等于配置的确认数，则更新充值单为成功并更新上链成功时间，否则只更新当前确认数。
            if(transaction.confirmations() >= coinInfo.getConfirms()) {
                depositDTO.setUpchainStatus(UpchainStatusEnum.SUCCESS.getCode());
                depositDTO.setUpchainSuccessAt(dateProvider.now());
            }
            depositDTO.setCurrentConfirm(transaction.confirmations());
            depositDTO.setUpdatedAt(dateProvider.now());

            depositService.updateRecharge(depositDTO.clone(OmsDeposit.class));

            //TODO 发送消息给用户账户增加余额
        }
    }

}
