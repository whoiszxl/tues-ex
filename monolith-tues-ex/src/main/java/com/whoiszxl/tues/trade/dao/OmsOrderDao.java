package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.trade.entity.OmsOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 挂单dao接口
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface OmsOrderDao {

    /**
     * 保存挂单信息
     * @param omsOrder
     */
    OmsOrder save(OmsOrder omsOrder);

    /**
     * 获取买方向的挂单列表
     * @param type 交易方向
     * @param memberId 用户ID
     * @param coinId 交易对第一个币种ID
     * @param replaceCoinId 交易对第二个币种ID
     * @param price 价格
     * @return
     */
    List<OmsOrder> getBuyMatchOrderList(Integer type, Long memberId, Integer coinId, Integer replaceCoinId, BigDecimal price);


    /**
     * 获取卖方向的挂单列表
     * @param type 交易方向
     * @param memberId 用户ID
     * @param coinId 交易对第一个币种ID
     * @param replaceCoinId 交易对第二个币种ID
     * @param price 价格
     * @return
     */
    List<OmsOrder> getSellMatchOrderList(Integer type, Long memberId, Integer coinId, Integer replaceCoinId, BigDecimal price);

    /**
     * 修改挂单的数量和状态
     * @param orderCount 挂单数量
     * @param status 状态
     * @param id 记录ID
     */
    void changeCountAndStatus(BigDecimal orderCount, Integer status, Long id);

    /**
     * 通过用户ID降序查询列表
     * @param memberId 用户ID
     * @return
     */
    List<OmsOrder> findAllByMemberIdOrderByIdDesc(Long memberId);


    /**
     * 通过订单ID查询订单
     * @param id 订单ID
     * @return
     */
    OmsOrder findById(Long id);

    /**
     * 更新订单信息
     * @param order 订单信息
     */
    OmsOrder updateOrder(OmsOrder order);

    /**
     * 通过订单ID和会员ID查找用户订单
     * @param id 订单ID
     * @param memberId 用户ID
     * @return
     */
    OmsOrder findByIdAndMemberId(Long id, Long memberId);
}
