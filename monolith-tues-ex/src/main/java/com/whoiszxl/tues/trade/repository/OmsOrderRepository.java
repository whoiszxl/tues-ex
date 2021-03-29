package com.whoiszxl.tues.trade.repository;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.trade.entity.OmsOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 挂单repository服务
 *
 * @author zhouxiaolong
 * @date 2021/3/26
 */
public interface OmsOrderRepository extends BaseRepository<OmsOrder> {


    @Query(value = "select * from oms_order " +
                    "where status = 2 and type != :type and member_id != :memberId " +
                    "and coin_id = :coinId and replace_coin_id = :replaceCoinId and current_count > 0 " +
                    "and price <= :price " +
                    "order by price asc", nativeQuery = true)
    List<OmsOrder> getBuyMatchOrderList(@Param("type") Integer type,
                                        @Param("memberId") Long memberId,
                                        @Param("coinId") Integer coinId,
                                        @Param("replaceCoinId") Integer replaceCoinId,
                                        @Param("price") BigDecimal price);


    @Query(value = "select * from oms_order " +
                    "where status = 2 " +
                    "and type != :type " +
                    "and member_id != :memberId " +
                    "and coin_id = :coinId " +
                    "and replace_coin_id = :replaceCoinId " +
                    "and current_count > 0 " +
                    "and price >= :price " +
                    "order by price desc", nativeQuery = true)
    List<OmsOrder> getSellMatchOrderList(@Param("type") Integer type,
                                         @Param("memberId") Long memberId,
                                         @Param("coinId") Integer coinId,
                                         @Param("replaceCoinId") Integer replaceCoinId,
                                         @Param("price") BigDecimal price);

    @Transactional
    @Modifying
    @Query(value = "update oms_order " +
                    "set current_count = current_count - :orderCount, " +
                    "status = :status , " +
                    "updated_at = now() " +
                    "where id = :id", nativeQuery = true)
    Integer changeCountAndStatus(@Param("orderCount") BigDecimal orderCount,
                                 @Param("status") Integer status,
                                 @Param("id") Long id);


    /**
     * 通过用户ID降序查询列表
     * @param memberId 用户ID
     * @return
     */
    List<OmsOrder> findAllByMemberIdOrderByIdDesc(Long memberId);
}
