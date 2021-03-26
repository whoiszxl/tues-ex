package com.whoiszxl.tues.trade.controller;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.utils.JwtUtils;
import com.whoiszxl.tues.trade.entity.param.TransactionParam;
import com.whoiszxl.tues.trade.service.TransactionService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 交易管理接口
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Api(tags = "交易管理接口")
@RestController
@CrossOrigin
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TransactionService transactionsService;

    @PostMapping("/add")
    public Result addTransaction(@RequestBody TransactionParam transactionParam) {
        Claims memberClaims = JwtUtils.getUserClaims(request);
        Long memberId = Long.parseLong(memberClaims.getId());
        transactionParam.setMemberId(memberId);

        boolean flag = transactionsService.add(transactionParam);
        if(flag) {
            return Result.buildSuccess();
        }
        return Result.buildError();

    }

}
