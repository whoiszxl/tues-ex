package com.whoiszxl.tues.trade.controller;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.common.utils.JwtUtils;
import com.whoiszxl.tues.member.entity.dto.UmsMemberWalletDTO;
import com.whoiszxl.tues.member.entity.vo.UmsMemberWalletVO;
import com.whoiszxl.tues.member.service.MemberWalletService;
import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsDepositDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsWithdrawalDTO;
import com.whoiszxl.tues.common.bean.PageParam;
import com.whoiszxl.tues.trade.entity.vo.OmsCoinVO;
import com.whoiszxl.tues.trade.entity.vo.OmsDepositVO;
import com.whoiszxl.tues.trade.entity.vo.OmsWithdrawalVO;
import com.whoiszxl.tues.trade.service.CoinService;
import com.whoiszxl.tues.trade.service.DepositService;
import com.whoiszxl.tues.trade.service.WithdrawalService;
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

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private MemberWalletService memberWalletService;

    @PostMapping("/coinList")
    @ApiOperation(value = "币种列表接口", notes = "币种列表接口", response = OmsCoinVO.class)
    public Result<List<OmsCoinVO>> coinList(@RequestBody PageParam pageParam){
        List<OmsCoinDTO> coinDTOList =  coinService.coinList(pageParam);
        List<OmsCoinVO> omsCoinVOList = BeanCopierUtils.copyListProperties(coinDTOList, OmsCoinVO::new);
        return Result.buildSuccess(omsCoinVOList);
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取用户资产列表", notes = "获取用户资产列表", response = UmsMemberWalletVO.class)
    public Result<List<UmsMemberWalletVO>> assetList() {
        String memberId = JwtUtils.getUserClaims(request).getId();
        List<UmsMemberWalletDTO> memberWalletDTOList = memberWalletService.getAssetList(Long.parseLong(memberId));
        List<UmsMemberWalletVO> umsMemberWalletVOList = BeanCopierUtils.copyListProperties(memberWalletDTOList, UmsMemberWalletVO::new);
        return Result.buildSuccess(umsMemberWalletVOList);
    }

    @PostMapping("/deposit/list")
    @ApiOperation(value = "获取用户充值记录列表", notes = "获取用户充值记录列表", response = OmsDepositVO.class)
    public Result<List<OmsDepositVO>> depositList(@RequestBody PageParam pageParam) {
        String memberId = JwtUtils.getUserClaims(request).getId();
        List<OmsDepositDTO> depositDTOList = depositService.depositList(Long.parseLong(memberId), pageParam);
        List<OmsDepositVO> depositVOList = BeanCopierUtils.copyListProperties(depositDTOList, OmsDepositVO::new);
        return Result.buildSuccess(depositVOList);
    }

    @PostMapping("/withdraw/list")
    @ApiOperation(value = "获取用户提现记录列表", notes = "获取用户提现记录列表", response = OmsWithdrawalVO.class)
    public Result<List<OmsWithdrawalVO>> withdrawList(@RequestBody PageParam pageParam) {
        String memberId = JwtUtils.getUserClaims(request).getId();
        List<OmsWithdrawalDTO> withdrawalDTOList = withdrawalService.withdrawList(Long.parseLong(memberId), pageParam);
        List<OmsWithdrawalVO> withdrawalVOList = BeanCopierUtils.copyListProperties(withdrawalDTOList, OmsWithdrawalVO::new);
        return Result.buildSuccess(withdrawalVOList);
    }
}
