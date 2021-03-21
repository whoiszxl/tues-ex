package com.whoiszxl.tues.cms.service;

import com.whoiszxl.tues.cms.entity.dto.CmsBannerDTO;

import java.util.List;

/**
 * 主页服务
 */
public interface HomeService {

    /**
     * 获取轮播图列表
     * @param type 类型
     * @return
     */
    List<CmsBannerDTO> bannerList(Integer type);
}
