package com.whoiszxl.config;

import com.whoiszxl.orderbook.MemoryOrderBook;
import com.whoiszxl.orderbook.OrderBook;
import com.whoiszxl.orderbook.OrderBookFactory;
import com.whoiszxl.repository.OmsPair;
import com.whoiszxl.repository.OmsPairRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

/**
 * 订单簿工厂配置
 */
@Slf4j
@Configuration
public class OrderBookFactoryConfig {

    @Autowired
    private OmsPairRepository pairRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    public OrderBookFactory orderBookFactory() {
        OrderBookFactory orderBookFactory = OrderBookFactory.getInstance();
        List<OmsPair> pairList = pairRepository.findAllByStatusOrderBySortAscIdAsc(1);

        for (OmsPair omsPair : pairList) {
            OrderBook orderBook = new MemoryOrderBook(omsPair.getPairName());
            orderBook.setKafkaTemplate(kafkaTemplate);
            orderBook.setFirstCoinDecimals(omsPair.getCoinDecimals());
            orderBook.setSecondCoinDecimals(omsPair.getReplaceCoinDecimals());
            orderBook.setReady(true);
            orderBookFactory.addOrderBook(omsPair.getPairName(), orderBook);

        }

        return orderBookFactory;
    }
}
