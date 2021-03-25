package com.whoiszxl.tues.trade.dao;

import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.trade.entity.OmsCoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 币种dao服务
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
public interface CoinDao {

    /**
     * 通过状态查询币种列表 - 按sort与id升序
     * @param status 状态值
     * @return
     */
    List<OmsCoin> findAllByStatusOrderBySortAscIdAsc(Integer status);

    /**
     * 通过状态查询币种列表 - 按sort与id升序 - 分页
     * @param status 状态值
     * @param pageable 分页参数
     * @return
     */
    Page<OmsCoin> findAllByStatusOrderBySortAscIdAsc(Integer status, Pageable pageable);
}
