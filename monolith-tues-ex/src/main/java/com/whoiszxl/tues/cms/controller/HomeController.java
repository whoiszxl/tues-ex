package com.whoiszxl.tues.cms.controller;

import com.whoiszxl.tues.cms.entity.dto.CmsBannerDTO;
import com.whoiszxl.tues.cms.entity.vo.CmsBannerVO;
import com.whoiszxl.tues.cms.service.HomeService;
import com.whoiszxl.tues.common.bean.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页接口
 *
 * @author zhouxiaolong
 * @date 2021/3/21
 */
@RestController
@CrossOrigin
@RequestMapping("/home")
@Api(tags = "主页接口")
public class HomeController {

    @Autowired
    private HomeService homeService;


    @ApiOperation(value = "轮播图列表", notes = "轮播图列表", response = CmsBannerVO.class)
    @GetMapping("/banner/{type}")
    public Result<List<CmsBannerVO>> banner(@PathVariable("type") Integer type) {

        List<CmsBannerDTO> cmsBannerDTOS = homeService.bannerList(type);

        List<CmsBannerVO> cmsBannerVOList = new ArrayList<>(cmsBannerDTOS.size());
        for (CmsBannerDTO cmsBannerDTO : cmsBannerDTOS) {
            cmsBannerVOList.add(cmsBannerDTO.clone(CmsBannerVO.class));
        }

        return Result.buildSuccess(cmsBannerVOList);
    }

}
