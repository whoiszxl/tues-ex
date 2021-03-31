package com.whoiszxl.tues.trade.controller;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.trade.entity.dto.OmsPairDTO;
import com.whoiszxl.tues.trade.entity.vo.OmsPairVO;
import com.whoiszxl.tues.trade.service.PairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 交易对管理接口
 */
@Api(tags = "交易对接口")
@RestController
@CrossOrigin
@RequestMapping("/pair")
public class PairController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PairService pairService;

    @PostMapping("/list")
    @ApiOperation(value = "获取交易对列表", notes = "获取交易对列表", response = OmsPairVO.class)
    public Result<List<OmsPairVO>> pairList() {
        List<OmsPairDTO> pairDTOList = pairService.pairList();
        List<OmsPairVO> omsPairVOList = BeanCopierUtils.copyListProperties(pairDTOList, OmsPairVO::new);
        return Result.buildSuccess(omsPairVOList);
    }

}
