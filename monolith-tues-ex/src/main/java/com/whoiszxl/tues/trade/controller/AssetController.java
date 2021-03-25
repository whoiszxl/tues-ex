package com.whoiszxl.tues.trade.controller;

import com.aliyuncs.CommonResponse;
import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.trade.entity.param.PageParam;
import com.whoiszxl.tues.trade.entity.vo.OmsCoinVO;
import com.whoiszxl.tues.trade.service.CoinService;
import com.whoiszxl.tues.trade.service.DepositService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 资产管理接口
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Api(tags = "资产管理接口")
@RestController
@CrossOrigin
@RequestMapping("/asset")
public class AssetController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CoinService coinService;

    @Autowired
    private DepositService depositService;

    @PostMapping("/coinList")
    @ApiOperation(value = "币种列表接口", notes = "币种列表接口", response = OmsCoinVO.class)
    public Result<List<OmsCoinVO>> coinList(@RequestBody PageParam pageParam){
        List<OmsCoinDTO> coinDTOList =  coinService.coinList(pageParam);
        List<OmsCoinVO> omsCoinVOList = BeanCopierUtils.copyListProperties(coinDTOList, OmsCoinVO::new);
        return Result.buildSuccess(omsCoinVOList);
    }
}
