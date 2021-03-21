package com.whoiszxl.tues.cms.repository;

import com.whoiszxl.tues.cms.entity.CmsBanner;
import com.whoiszxl.tues.common.bean.BaseRepository;
import com.whoiszxl.tues.member.entity.UmsMember;

import java.util.List;

/**
 * 轮播图repository
 */
public interface CmsBannerRepository extends BaseRepository<CmsBanner> {

    /**
     * 通过类型和状态查找轮播图列表
     * @param type 类型 0->PC首页轮播；1->app首页轮播
     * @param status 0->下线；1->上线
     * @return 轮播图列表
     */
    List<CmsBanner> findAllByTypeAndStatus(Integer type, Integer status);

}
