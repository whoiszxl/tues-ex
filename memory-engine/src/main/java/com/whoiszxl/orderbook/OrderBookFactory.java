package com.whoiszxl.orderbook;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 订单簿创建工厂
 */
public class OrderBookFactory {

    /**
     * 存储订单簿的hash map, 交易对存在多个，所以需要多个订单簿来进行撮合交易
     */
    private ConcurrentHashMap<String, OrderBook> orderBookMap;


    private static OrderBookFactory instance;
    public static OrderBookFactory getInstance() {
        if(instance == null) {
            instance = new OrderBookFactory();
            instance.orderBookMap = new ConcurrentHashMap<>();
        }
        return instance;
    }

    /**
     * 添加一个订单簿到map中
     * @param pairName 交易对名称
     * @param orderBook 订单簿
     */
    public void addOrderBook(String pairName, OrderBook orderBook) {
        if (!orderBookMap.containsKey(pairName)) {
            orderBookMap.put(pairName, orderBook);
        }
    }

    /**
     * 重设订单簿
     * @param pairName 交易对名称
     * @param orderBook 订单簿
     */
    public void resetOrderBook(String pairName, OrderBook orderBook) {
        orderBookMap.put(pairName, orderBook);
    }

    /**
     * 判断容器是否包含此交易对的订单簿
     * @param pairName 交易对名称
     * @return
     */
    public boolean containsOrderBook(String pairName) {
        return orderBookMap.containsKey(pairName);
    }

    /**
     * 通过交易对名称获取订单簿
     * @param pairName 订单簿名称
     * @return
     */
    public OrderBook getOrderBook(String pairName) {
        return orderBookMap.get(pairName);
    }

    /**
     * 获取所有订单簿
     * @return 所有订单簿
     */
    public ConcurrentMap<String, OrderBook> getOrderBookMap() {
        return orderBookMap;
    }
}
