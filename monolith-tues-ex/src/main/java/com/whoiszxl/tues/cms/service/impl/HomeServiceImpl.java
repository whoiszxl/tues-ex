package com.whoiszxl.tues.cms.service.impl;

import com.whoiszxl.tues.cms.dao.CmsBannerDao;
import com.whoiszxl.tues.cms.entity.CmsBanner;
import com.whoiszxl.tues.cms.entity.dto.CmsBannerDTO;
import com.whoiszxl.tues.cms.service.HomeService;
import com.whoiszxl.tues.common.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页服务接口实现
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private CmsBannerDao cmsBannerDao;

    @Override
    public List<CmsBannerDTO> bannerList(Integer type) {
        List<CmsBanner> cmsBannerList = cmsBannerDao.findAllByTypeAndStatus(type, StatusEnum.OPEN.getCode());

        List<CmsBannerDTO> cmsBannerDTOList = new ArrayList<>(cmsBannerList.size());
        for (CmsBanner cmsBanner : cmsBannerList) {
            cmsBannerDTOList.add(cmsBanner.clone(CmsBannerDTO.class));
        }
        return cmsBannerDTOList;
    }
}
