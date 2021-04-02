package com.whoiszxl.tues.trade.controller;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.common.utils.JwtUtils;
import com.whoiszxl.tues.trade.entity.dto.OmsOrderDTO;
import com.whoiszxl.tues.trade.entity.param.CancelOrderParam;
import com.whoiszxl.tues.trade.entity.param.OrderParam;
import com.whoiszxl.tues.trade.entity.vo.OmsOrderVO;
import com.whoiszxl.tues.trade.service.OrderService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 交易管理接口
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Api(tags = "交易管理接口")
@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    @ApiOperation(value = "下单", notes = "挂单到挂单表中，并进行一次数据库撮合", response = Result.class)
    public Result addOrder(@RequestBody OrderParam orderParam) {
        Claims memberClaims = JwtUtils.getUserClaims(request);
        Long memberId = Long.parseLong(memberClaims.getId());
        orderParam.setMemberId(memberId);
        return orderService.add(orderParam) ? Result.buildSuccess() : Result.buildError();
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "撤单", notes = "撤销订单", response = Result.class)
    public Result cancelOrder(@RequestBody CancelOrderParam cancelOrderParam) {
        Claims memberClaims = JwtUtils.getUserClaims(request);
        Long memberId = Long.parseLong(memberClaims.getId());
        return orderService.cancelOrder(cancelOrderParam.getOrderId(), memberId);
    }

    @PostMapping("/list")
    @ApiOperation(value = "查看当前挂单列表", notes = "查看当前挂单列表", response = OmsOrderVO.class)
    public Result<List<OmsOrderVO>> listOrder() {
        Claims memberClaims = JwtUtils.getUserClaims(request);
        Long memberId = Long.parseLong(memberClaims.getId());

        List<OmsOrderDTO> orderDTOList = orderService.listOrder(memberId);
        List<OmsOrderVO> orderVOList = BeanCopierUtils.copyListProperties(orderDTOList, OmsOrderVO::new);
        return Result.buildSuccess(orderVOList);
    }

}
