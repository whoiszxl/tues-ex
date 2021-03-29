package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.dto.OmsDealDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsOrderDTO;
import com.whoiszxl.tues.trade.entity.param.OrderParam;

import java.util.List;

/**
 * 挂单服务
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface OrderService {

    /**
     * 挂单
     * @param orderParam 挂单参数
     * @return
     */
    boolean add(OrderParam orderParam);

    /**
     * 查看指定用户的当前挂单列表
     * @param memberId 用户ID
     * @return
     */
    List<OmsOrderDTO> listOrder(Long memberId);

    /**
     * 查看已成交详情列表
     * @param memberId 用户ID
     * @param orderId 挂单ID
     * @return
     */
    List<OmsDealDTO> listDeal(Long memberId, Long orderId);
}
