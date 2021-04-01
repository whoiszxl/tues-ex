package com.whoiszxl.orderbook;

import com.whoiszxl.constants.BuySellEnum;
import com.whoiszxl.entity.BucketMatchResult;
import com.whoiszxl.entity.ExDeal;
import com.whoiszxl.entity.ExOrder;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.core.convert.Bucket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

/**
 * 内存订单桶实现
 */
@EqualsAndHashCode
public class MemoryOrderBucket implements OrderBucket {

    /** 订单桶价格 */
    private BigDecimal price;

    /** 当前价格的总量 */
    private BigDecimal totalCount = BigDecimal.ZERO;

    private LinkedHashMap<BigDecimal, ExOrder> orders = new LinkedHashMap<>();


    @Override
    public void addOrder(ExOrder exOrder) {
        orders.put(exOrder.getPrice(), exOrder);
        //将通过了预撮合的订单剩余的数量加到订单桶的总量中去
        totalCount = totalCount.add(exOrder.getCurrentCount());
    }

    @Override
    public ExOrder removeOrder(Long orderId) {
        ExOrder exOrder = orders.get(orderId);
        if(exOrder == null) {
            return null;
        }
        orders.remove(orderId);

        //移除订单后需要将订单剩余数量从订单桶总量中减去
        totalCount = totalCount.subtract(exOrder.getCurrentCount());
        return exOrder;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public BigDecimal getTotalCount() {
        return totalCount;
    }

    /**
     * 假设当前有两个卖单桶：
     * 卖单桶one -> 卖价20元 -> 单数5单,数量分别为：[10,20,30,40,50]
     * 卖单桶two -> 卖价30元 -> 单数5单,数量分别为：[1,2,3,4,5]
     * 我发起一个买单，买价25元，买60的量，那么这笔买单就会去卖单桶one里进行匹配。(价格优先，时间优先原则)
     * 会从卖单桶里吃掉[10,20,30]这三笔单，然后实行（多退少补）原则，退 "(买价-卖价)*量" 元, 则为: (25-20)*60=300元
     *
     * @param matchCount 匹配撮合的量
     * @param targetOrder 交易信息
     * @param removeOrderCallback 移除订单缓存的回调事件
     * @return
     */
    @Override
    public BucketMatchResult match(BigDecimal matchCount, ExOrder targetOrder, Consumer<ExOrder> removeOrderCallback) {
        Iterator<Map.Entry<BigDecimal, ExOrder>> iterator = orders.entrySet().iterator();
        BigDecimal allMatchCount = BigDecimal.ZERO;

        List<ExDeal> dealList = new ArrayList<>();
        List<ExOrder> complatedOtherOrderList = new ArrayList<>();

        while(iterator.hasNext() && matchCount.compareTo(BigDecimal.ZERO) > 0) {
            //遍历拿到订单桶中的订单详情
            Map.Entry<BigDecimal, ExOrder> next = iterator.next();
            ExOrder otherOrder = next.getValue();

            //计算流转过来的订单可以在订单桶中的这笔单中吃多少量
            BigDecimal tradedCount = matchCount.min(targetOrder.getCurrentCount());
            allMatchCount = allMatchCount.add(tradedCount);

            //更新可交易数量
            otherOrder.setCurrentCount(otherOrder.getCurrentCount().subtract(tradedCount));
            //更新流转订单需要撮合的数量
            matchCount = matchCount.subtract(tradedCount);
            //更新订单桶总委托量
            totalCount = totalCount.subtract(tradedCount);

            //获取订单桶中的这笔订单是否已经被全部交易完毕了
            boolean fullMatch = otherOrder.getCurrentCount().compareTo(BigDecimal.ZERO) == 0;

            //获取成交价
            BigDecimal tradedPrice = targetOrder.getPrice().min(otherOrder.getPrice());

            //更新成交额
            //成交数量乘以被撮合单的价格
            BigDecimal turnover = tradedCount.multiply(tradedPrice);
            targetOrder.setTurnover(targetOrder.getTurnover().add(turnover));
            otherOrder.setTurnover(otherOrder.getTurnover().add(turnover));
            targetOrder.setVolume(targetOrder.getVolume().add(tradedCount));
            otherOrder.setVolume(otherOrder.getVolume().add(tradedCount));

            //生成成交记录
            ExDeal deal = new ExDeal();
            deal.setPairName(targetOrder.getPairName());
            deal.setSuccessCount(tradedCount);
            deal.setDirection(targetOrder.getDirection());
            deal.setPrice(otherOrder.getPrice());
            deal.setCoinId(targetOrder.getCoinId());
            deal.setReplaceCoinId(targetOrder.getReplaceCoinId());
            deal.setTime(LocalDateTime.now());
            if(BuySellEnum.BUY.getValue().equals(targetOrder.getDirection())) {
                deal.setBuyOrderId(targetOrder.getOrderId());
                deal.setSellOrderId(otherOrder.getOrderId());
            }else {
                deal.setBuyOrderId(otherOrder.getOrderId());
                deal.setSellOrderId(targetOrder.getOrderId());
            }
            dealList.add(deal);

            if(fullMatch) {
                complatedOtherOrderList.add(otherOrder);

                //从订单Id缓存中删除这笔交易并将此迭代的订单从桶中移除
                removeOrderCallback.accept(otherOrder);
                iterator.remove();
            }
        }
        return new BucketMatchResult(dealList, complatedOtherOrderList, allMatchCount);
    }

}
