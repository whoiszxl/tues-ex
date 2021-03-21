package com.whoiszxl.tues.cms.dao;

import com.whoiszxl.tues.cms.entity.CmsBanner;

import java.util.List;

public interface CmsBannerDao {

    /**
     * 通过类型和状态查找轮播图列表
     * @param type 类型 0->PC首页轮播；1->app首页轮播
     * @param status 0->下线；1->上线
     * @return 轮播图列表
     */
    List<CmsBanner> findAllByTypeAndStatus(Integer type, Integer status);
}
