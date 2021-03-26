package com.whoiszxl.tues.trade.service;

import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.common.bean.PageParam;

import java.util.List;

/**
 * 币种服务接口
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
public interface CoinService {

    /**
     * 通过状态查询币种列表 - 按sort与id升序
     * @param status 状态值
     * @return
     */
    List<OmsCoinDTO> findAllByStatusOrderBySortAscIdAsc(Integer status);

    /**
     * 通过币种名称获取币种信息
     * @param name 币种名称
     * @return 币种信息
     */
    OmsCoinDTO findCoinByName(String name);

    /**
     * 分页查询币种列表
     * @param pageParam
     * @return
     */
    List<OmsCoinDTO> coinList(PageParam pageParam);
}
