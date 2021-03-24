package com.whoiszxl.tues.wallet.controller;

import com.aliyuncs.CommonResponse;
import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.utils.JwtUtils;
import com.whoiszxl.tues.member.entity.dto.UmsMemberAddressDTO;
import com.whoiszxl.tues.member.entity.vo.UmsMemberAddressVO;
import com.whoiszxl.tues.wallet.entity.AddressDispenseParam;
import com.whoiszxl.tues.wallet.service.AddressDispenseService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 地址分配控制器
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Api(tags = "地址分配接口")
@RestController
@CrossOrigin
@RequestMapping("/address")
public class AddressDispenseController {

    @Autowired
    private AddressDispenseService addressDispenseService;

    @Autowired
    private HttpServletRequest request;


    @ApiOperation(value = "通过coinId分配或获取一个地址", notes = "通过coinId分配或获取一个地址", response = UmsMemberAddressVO.class)
    @PostMapping("/dispense")
    public Result<UmsMemberAddressVO> addressDispense(@RequestBody @Valid AddressDispenseParam addressDispenseParam) {
        //从jwt获取用户ID
        Claims userClaims = JwtUtils.getUserClaims(request);
        Long memberId = Long.parseLong(userClaims.getId());

        //创建或获取地址
        UmsMemberAddressDTO umsMemberAddressDTO = addressDispenseService.createOrGetAddress(memberId, addressDispenseParam.getCoinId());
        return Result.buildSuccess(umsMemberAddressDTO.clone(UmsMemberAddressVO.class));
    }


}
