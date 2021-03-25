package com.whoiszxl.tues.wallet.ethereum.core.service;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.utils.AssertUtils;
import com.whoiszxl.tues.wallet.ethereum.core.entity.EthereumAddress;
import com.whoiszxl.tues.wallet.ethereum.core.utils.EthereumUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.crypto.*;
import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import sun.security.provider.SecureRandom;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * 以太坊服务，支持ERC2O和ETH
 *
 * @author whoiszxl
 * @date 2021/3/23
 */
@Slf4j
@Service
public class EthereumService {

    @Value("${coin.eth.keystorepath}")
    private String keystorePath;

    @Value("${coin.eth.keystorepassword}")
    private String keystorePassword;

    @Value("{coin.eth.gaslimit}")
    private String gasLimit;

    /**
     * erc20中转账事件签名的hash值，是eventLog中的topics[0]字段。
     * 其通过keccak算法加密"Transfer(address,address,uint256)"得到结果
     * 在线加密地址：https://emn178.github.io/online-tools/keccak_256.html
     */
    protected String transferEventSignature = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";

    @Autowired
    private Web3j web3j;


    private static final List<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH = Arrays.asList(new ChildNumber(44, true), new ChildNumber(60, true), ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);


    /**
     * 通过keystore文件生成地址
     * @return 地址与keystore文件名
     */
    public EthereumAddress createAddressByFile() {
        try {
            String fileName = WalletUtils.generateNewWalletFile(keystorePassword, new File(keystorePath), false);
            Credentials credentials = WalletUtils.loadCredentials(keystorePassword, keystorePath + File.separator + fileName);
            String address = credentials.getAddress();
            return new EthereumAddress(address, fileName, null);
        } catch (Exception e) {
            log.error("通过keystore文件生成地址发生异常：", e);
            return null;
        }
    }

    /**
     * 通过助记词生成地址和私钥
     * @return
     */
    public String createAddressByMnemonic() {

        try {
            //1. 生成安全随机数
            SecureRandom secureRandom = new SecureRandom();
            byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
            secureRandom.engineNextBytes(entropy);

            //2. 通过安全随机数生成12位助记词
            List<String> mnemonics = MnemonicCode.INSTANCE.toMnemonic(entropy);

            //3. 通过助记词生成钱包种子
            byte[] seed = MnemonicCode.toSeed(mnemonics, keystorePassword);

            //4. 生成公私钥对
            DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
            DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
            DeterministicKey deterministicKey = deterministicHierarchy.deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
            byte[] bytes = deterministicKey.getPrivKeyBytes();
            ECKeyPair keyPair = ECKeyPair.create(bytes);

            //5. 通过公钥生成地址
            String address = Keys.getAddress(keyPair.getPublicKey());
            log.info("Mnemonic：" + mnemonics);
            log.info("Address：0x" + address);
            log.info("PrivateKey：0x" + keyPair.getPrivateKey().toString(16));
            log.info("PublicKey：" + keyPair.getPublicKey().toString(16));

        } catch (MnemonicException.MnemonicLengthException e) {
            log.error("通过助记词生成地址发生异常：", e);
        }
        return "";
    }


    /**
     * 获取地址的ETH余额
     * @param address ETH地址
     * @return ETH余额
     */
    public BigDecimal getEthBalance(String address) {
        AssertUtils.isTrue(EthereumUtils.isVaildAddress(address), "ETH地址无效");
        try {
            EthGetBalance ethBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return Convert.fromWei(ethBalance.getBalance().toString(), Convert.Unit.ETHER);
        } catch (IOException e) {
            log.error("通过助记词生成地址发生异常：", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * 获取地址的token余额
     * @param address token地址
     * @param contractAddress 合约地址
     * @return
     */
    public BigDecimal getTokenBalance(String address, String contractAddress) {
        //1. 校验参数是否有效
        AssertUtils.isTrue(EthereumUtils.isVaildAddress(address), "token地址无效");
        AssertUtils.isTrue(EthereumUtils.isVaildAddress(contractAddress), "token合约地址无效");

        try {
            //2. 创建一个balanceOf方法通过ethCall方法调用
            Function balanceOf = new Function("balanceOf", Arrays.asList(new Address(address)), Collections.<TypeReference<?>>emptyList());
            EthCall ethCall = web3j.ethCall(Transaction.createEthCallTransaction(address, contractAddress, FunctionEncoder.encode(balanceOf)), DefaultBlockParameterName.PENDING).send();
            if(ethCall.hasError()) {
                log.error("获取token余额失败：", ethCall.getError().getMessage());
                return BigDecimal.ZERO;
            }

            //3. 获取请求的结果和token的精度，进行移位得到实际金额
            String value = ethCall.getValue();
            String balance = Numeric.toBigInt(value).toString();
            Integer decimal = getTokenDecimal(contractAddress);

            return new BigDecimal(balance).movePointLeft(decimal);
        } catch (Exception e) {
            log.error("获取token余额失败：", e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取token的精度
     * @param contractAddress token合约地址
     * @return
     * @throws IOException
     */
    public Integer getTokenDecimal(String contractAddress) throws IOException {
        Function function = new Function("decimals", Arrays.asList(), Collections.<TypeReference<?>>emptyList());
        EthCall ethCall = web3j.ethCall(Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send();
        if (ethCall.hasError()) {
            log.error("获取token精度失败", ethCall.getError().getMessage());
            return null;
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        Integer decimals = Integer.parseInt(decode.get(0).getValue().toString());
        return decimals;
    }

    /**
     * 获取网络区块高度
     * @return 区块高度
     */
    public Long getBlockchainHeight() {
        try {
            EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
            return blockNumber.getBlockNumber().longValue();
        } catch (IOException e) {
            log.error("获取区块高度失败", e);
        }
        return 0L;
    }

    /**
     * 通过高度获取区块详细信息
     * @param height 区块高度
     * @return
     */
    public EthBlock.Block getBlockByNumber(Long height) {
        try {
            EthBlock block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(height), true).send();
            return block.getBlock();
        } catch (IOException e) {
            log.error("通过高度获取区块详细信息失败", e);
        }
        return null;
    }

    /**
     * 通过交易Hash获取交易详情
     * @param txId 交易Hash
     * @return
     */
    public org.web3j.protocol.core.methods.response.Transaction getTransactionByHash(String txId) {
        try {
            return web3j.ethGetTransactionByHash(txId).send().getTransaction().orElse(null);
        } catch (IOException e) {
            log.error("通过交易Hash获取交易详情失败", e);
        }
        return null;
    }


    /**
     * 获取gas价格
     * @return
     */
    public BigInteger getGasPrice() {
        try {
            EthGasPrice gasPrice = web3j.ethGasPrice().send();
            return gasPrice.getGasPrice();
        } catch (IOException e) {
            log.error("获取gas价格失败", e);
        }
        return null;
    }

    /**
     * 通过交易哈希校验交易是否有效
     * @param txId 交易哈希
     * @return
     */
    public boolean validateTransaction(String txId) {
        try {
            //1. 通过交易哈希获取到交易信息
            org.web3j.protocol.core.methods.response.Transaction tx = getTransactionByHash(txId);
            if(ObjectUtils.isEmpty(tx)) {
                return false;
            }

            //2. 校验交易的区块Hash字段是否有效
            if(StringUtils.isEmpty(tx.getBlockHash()) || Numeric.toBigInt(tx.getBlockHash()).compareTo(BigInteger.ZERO) == 0) {
                return false;
            }

            //3. 通过交易哈希获取到交易的收据信息
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(txId).send();
            if(ethGetTransactionReceipt == null || ethGetTransactionReceipt.hasError() || ethGetTransactionReceipt.getTransactionReceipt().isPresent()) {
                log.error("通过txId获取交易收入失败");
                return false;
            }

            //4. 判断收入信息的status字段是否有效
            Optional<TransactionReceipt> receiptOpt = ethGetTransactionReceipt.getTransactionReceipt();
            if(!receiptOpt.isPresent()) {
                return false;
            }
            TransactionReceipt receipt = receiptOpt.get();
            if(!receipt.getStatus().equalsIgnoreCase("0x1")) {
                return true;
            }
        } catch (IOException e) {
            log.error("获取gas价格失败", e);
        }
        return false;

    }

    /**
     * 通过交易哈希获取到交易的收据信息
     * @param txId 交易Hash
     */
    public TransactionReceipt getTransactionReceipt(String txId) {

        //通过交易哈希获取到交易的收据信息
        try {
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(txId).send();
            if(ethGetTransactionReceipt == null || ethGetTransactionReceipt.hasError() || !ethGetTransactionReceipt.getTransactionReceipt().isPresent()) {
                log.error("通过txId获取交易收入失败");
                return null;
            }
            Optional<TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
            if(transactionReceipt.isPresent()) {
                return transactionReceipt.get();
            }
            return null;
        } catch (IOException e) {
            log.error("通过txId获取交易收入失败", e);
        }
        return null;
    }

    /**
     * 校验事件日志防止Token假充值
     * https://mp.weixin.qq.com/s/3cMbE6p_4qCdVLa4FNA5-A
     * @param height 区块高度
     * @param contractAddress 合约地址
     * @param txId 交易HASH
     * @return
     */
    public boolean checkEventLog(Integer height, String contractAddress, String txId) {
        try {
            org.web3j.protocol.core.methods.request.EthFilter ethFilter =
                    new org.web3j.protocol.core.methods.request.EthFilter(
                            new DefaultBlockParameterNumber(height),
                            new DefaultBlockParameterNumber(height),
                            contractAddress
                    );
            ethFilter.addSingleTopic(transferEventSignature);
            EthLog ethLog = web3j.ethGetLogs(ethFilter).send();

            List<EthLog.LogResult> logResults = ethLog.getLogs();
            if(ObjectUtils.isEmpty(logResults)) {
                return false;
            }
            for (EthLog.LogResult logResult : logResults) {
                EthLog.LogObject logObject = (EthLog.LogObject) logResult;
                Log log = logObject.get();

                if(txId.equalsIgnoreCase(log.getTransactionHash())) {
                    return true;
                }
            }

        } catch (IOException e) {
            log.error("校验事件日志失败", e);
        }
        return false;
    }


    public Result<String> transfer(String walletFile, String password, String toAddress, BigDecimal amount) {
        //解锁钱包文件
        Credentials credentials;
        try {
            credentials = WalletUtils.loadCredentials(password, walletFile);
        } catch (IOException e) {
            log.error("钱包文件不存在", e);
            return Result.buildError("钱包文件不存在");
        } catch (CipherException e) {
            log.error("钱包密码不正确", e);
            return Result.buildError("钱包密码不正确");
        }

        //获得交易nonce
        BigInteger nonce = null;
        try {
            EthGetTransactionCount txCount = web3j.ethGetTransactionCount(
                    credentials.getAddress(),
                    DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            nonce = txCount.getTransactionCount();
        } catch (Exception e) {
            log.error("获取nonce失败", e);
        }

        //获取gas价格
        BigInteger gasPrice = getGasPrice();

        //发送金额 wei单位
        BigInteger weiValue = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

        //创建交易raw对象
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, gasPrice, new BigInteger(gasLimit), toAddress, weiValue);

        //对交易信息进行签名，并转为十六进制字符串
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        //将交易广播出去并获取到交易Hash
        try {
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String txHash = ethSendTransaction.getTransactionHash();
            log.info("transfer 产生了一笔ETH交易  from:{} to:{} amount:{} txHash:{}",
                    credentials.getAddress(), toAddress, amount.toPlainString(), txHash);

            if (StringUtils.isEmpty(txHash) || txHash.equals("null")) {
                return Result.buildError("交易发送失败");
            }

            return Result.buildSuccess(txHash);
        } catch (Exception e) {
            log.error("广播交易失败", e);
            return Result.buildError("广播交易失败");
        }
    }
}