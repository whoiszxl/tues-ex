package com.whoiszxl.orderbook;

import com.whoiszxl.constants.BuySellEnum;
import com.whoiszxl.constants.MessageTypeConstants;
import com.whoiszxl.entity.BucketMatchResult;
import com.whoiszxl.entity.ExDeal;
import com.whoiszxl.entity.ExOrder;
import com.whoiszxl.entity.Result;
import com.whoiszxl.utils.JsonUtil;
import io.netty.util.collection.LongObjectHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 基于内存的订单簿服务
 */
@Slf4j
public class MemoryOrderBook implements OrderBook {

    /** 交易对名称 */
    private String pairName;

    /** 限价卖单桶 */
    private NavigableMap<BigDecimal, OrderBucket> sellLimitPriceBuckets;
    /** 限价买单桶 */
    private NavigableMap<BigDecimal, OrderBucket> buyLimitPriceBuckets;
    /** 判断订单是否存在用的map */
    private LongObjectHashMap<ExOrder> orderIdMap;

    /** 是否暂停 */
    private boolean isPause = false;
    /** 是否就绪 */
    private boolean isReady = false;

    private KafkaTemplate<String, String> kafkaTemplate;

    /** 交易对第一个币种的精度 */
    private Integer firstCoinDecimals;

    /** 交易对第二个币种的精度 */
    private Integer secondCoinDecimals;

    public MemoryOrderBook(String pairName) {
        init(pairName);
    }

    void init(String pairName) {
        log.info("开始初始化{}交易对的订单簿", pairName);
        this.pairName = pairName;

        //限价卖单按价格升序排列，价格卖越低 优先级越高
        sellLimitPriceBuckets = new TreeMap<>(Comparator.naturalOrder());
        //限价买单按价格降序排列，价格买得越高 优先级越高
        buyLimitPriceBuckets = new TreeMap<>(Comparator.reverseOrder());

        orderIdMap = new LongObjectHashMap<>();
        isReady = true;
    }


    @Override
    public Result newOrder(ExOrder targetOrder) {
        //0. 校验参数
        Result checkResult = checkOrderParams(targetOrder);
        if(!checkResult.isOk()) {
            return checkResult;
        }

        //1. 判断是否已经存在此订单
        if(orderIdMap.containsKey(targetOrder.getOrderId())) {
            return Result.buildError(targetOrder.getOrderId() + "为重复订单");
        }

        //2. 获取到当前订单交易方向的对手单
        NavigableMap<BigDecimal, OrderBucket> otherBuckets = BuySellEnum.BUY.getValue().equals(targetOrder.getDirection()) ?
                        sellLimitPriceBuckets : buyLimitPriceBuckets;

        //3. 获取到订单价格匹配的对手单, 包括相等的订单
        otherBuckets = otherBuckets.headMap(targetOrder.getPrice(), true);

        //4. 进行预撮合
        BigDecimal matchCount = BigDecimal.ZERO;
        if(ObjectUtils.isNotEmpty(otherBuckets)) {
            matchCount = preMatch(targetOrder, otherBuckets);
        }

        //5. 进行预撮合后，更新当前可交易数量
        targetOrder.setCurrentCount(targetOrder.getTotalCount().subtract(matchCount));

        //7. 加入订单桶中
        NavigableMap<BigDecimal, OrderBucket> limitPriceBuckets
                = BuySellEnum.BUY.getValue().equals(targetOrder.getDirection()) ? buyLimitPriceBuckets : sellLimitPriceBuckets;

        //不存在就新增一个bucket
        OrderBucket orderBucket = limitPriceBuckets.computeIfAbsent(targetOrder.getPrice(), p -> {
            OrderBucket var = new MemoryOrderBucket();
            var.setPrice(p);
            return var;
        });
        targetOrder.setAddTime(LocalDateTime.now());
        orderBucket.addOrder(targetOrder);
        orderIdMap.put(targetOrder.getOrderId(), targetOrder);
        return Result.buildSuccess();
    }

    private Result checkOrderParams(ExOrder targetOrder) {
        if(isPause) {
            return Result.buildError(targetOrder.getPairName() + "撮合已暂停");
        }
        if(!pairName.equalsIgnoreCase(targetOrder.getPairName())) {
            return Result.buildError("订单簿不支持" + targetOrder.getPairName() + "交易对");
        }
        if(targetOrder.getCurrentCount().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.buildError("资金错误");
        }
        return Result.buildSuccess();
    }

    /**
     * 预撮合，当一笔订单流转到撮合引擎的时候，先进行一次与对手单的匹配，匹配过后再入订单簿中
     * 这样可以在有完全匹配的订单情况下，避免订单进入订单簿中。
     * @param targetOrder 订单信息
     * @param otherBuckets 对手单列表
     * @return
     */
    private BigDecimal preMatch(ExOrder targetOrder, NavigableMap<BigDecimal, OrderBucket> otherBuckets) {
        BigDecimal allCount = BigDecimal.ZERO;
        List<BigDecimal> emptyBuckets = new ArrayList<>();

        List<ExDeal> allDealList = new ArrayList<>();
        List<ExOrder> allCompletedOrderList = new ArrayList<>();

        for (OrderBucket bucket : otherBuckets.values()) {
            //获取到此次循环中需要撮合的量并在当前的订单桶中进行匹配
            BigDecimal matchCount = targetOrder.getCurrentCount().subtract(allCount);
            BucketMatchResult matchResult = bucket.match(
                    matchCount,
                    targetOrder,
                    otherOrder -> orderIdMap.remove(otherOrder.getOrderId()));

            BigDecimal alreadyMatch = matchResult.getAllMatchCount();
            allDealList.addAll(matchResult.getDealList());
            allCompletedOrderList.addAll(matchResult.getOrderList());


            //将已撮合数量加到当前预撮合的总量中去
            allCount = allCount.add(alreadyMatch);

            //如果订单桶的总量被撮合完则需要移除此订单桶
            if(bucket.getTotalCount().compareTo(BigDecimal.ZERO) == 0) {
                emptyBuckets.add(bucket.getPrice());
            }

            //如果当前预撮合的总量与此订单的数量一致了，就停止预撮合操作
            if(allCount.compareTo(targetOrder.getCurrentCount()) == 0) {
                break;
            }
        }

        //判断当前订单是否完成了撮合
        if(targetOrder.getCurrentCount().subtract(allCount).compareTo(BigDecimal.ZERO) == 0) {
            allCompletedOrderList.add(targetOrder);
        }

        //移除空订单桶
        emptyBuckets.forEach(otherBuckets::remove);

        //将结果发送到队列中进行处理
        if(ObjectUtils.isNotEmpty(allDealList)) {
            handleDealSuccess(allDealList);
        }
        if(ObjectUtils.isNotEmpty(allCompletedOrderList)) {
            handleOrderSuccess(allCompletedOrderList);
        }

        return allCount;
    }

    /**
     * 处理订单撮合成功事件
     * @param allCompletedOrderList
     */
    private void handleOrderSuccess(List<ExOrder> allCompletedOrderList) {
        int maxSize = 100;
        int handleSize = allCompletedOrderList.size();
        if(handleSize > maxSize) {
            for(int i = 0; i < handleSize; i = i + maxSize) {
                int length = (handleSize - i) > maxSize ? maxSize : handleSize - i;
                List<ExOrder> subList = allCompletedOrderList.subList(i, i + length);
                kafkaTemplate.send(MessageTypeConstants.HANDLE_ORDER_SUCCESS, JsonUtil.toJson(subList));
            }
        }else {
            kafkaTemplate.send(MessageTypeConstants.HANDLE_ORDER_SUCCESS, JsonUtil.toJson(allCompletedOrderList ));
        }

    }

    /**
     * 处理交易成功事件（交易详情）
     * @param allDealList
     */
    private void handleDealSuccess(List<ExDeal> allDealList) {
        int maxSize = 100;
        int handleSize = allDealList.size();
        if(handleSize > maxSize) {
            for(int i = 0; i < handleSize; i = i + maxSize) {
                int length = (handleSize - i) > maxSize ? maxSize : handleSize - i;
                List<ExDeal> subList = allDealList.subList(i, i + length);
                kafkaTemplate.send(MessageTypeConstants.HANDLE_DEAL_SUCCESS, JsonUtil.toJson(subList));
            }
        }else {
            kafkaTemplate.send(MessageTypeConstants.HANDLE_DEAL_SUCCESS, JsonUtil.toJson(allDealList));
        }
    }


    @Override
    public Result cancelOrder(Long orderId) {
        return null;
    }

    @Override
    public boolean getPause() {
        return isPause;
    }

    @Override
    public boolean getReady() {
        return isReady;
    }

    @Override
    public void setReady(boolean flag) {
        this.isReady = flag;
    }

    @Override
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void setFirstCoinDecimals(Integer decimals) {
        this.firstCoinDecimals = decimals;
    }

    @Override
    public void setSecondCoinDecimals(Integer decimals) {
        this.secondCoinDecimals = decimals;
    }
}
