package com.whoiszxl.tues.cms.dao.impl;

import com.whoiszxl.tues.cms.dao.CmsBannerDao;
import com.whoiszxl.tues.cms.entity.CmsBanner;
import com.whoiszxl.tues.cms.repository.CmsBannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsBannerDaoImpl implements CmsBannerDao {

    @Autowired
    private CmsBannerRepository cmsBannerRepository;

    @Override
    public List<CmsBanner> findAllByTypeAndStatus(Integer type, Integer status) {
        return cmsBannerRepository.findAllByTypeAndStatus(type, status);
    }
}
