package com.asluax.lease.web.app.controller.payment;


import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.PaymentType;
import com.asluax.lease.model.entity.RoomPaymentType;
import com.asluax.lease.web.app.service.PaymentTypeService;
import com.asluax.lease.web.app.service.RoomPaymentTypeService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "支付方式接口")
@RestController
@RequestMapping("/app/payment")
public class PaymentTypeController {

    @Autowired
    PaymentTypeService paymentTypeService;
    @Autowired
    RoomPaymentTypeService roomPaymentTypeService;

    @Operation(summary = "根据房间id获取可选支付方式列表")
    @GetMapping("listByRoomId")
    public Result<List<PaymentType>> list(@RequestParam Long id) {
        List<RoomPaymentType> roomPaymentTypeList = roomPaymentTypeService.lambdaQuery().eq(RoomPaymentType::getRoomId, id).list();
        List<Long> list = roomPaymentTypeList.stream().map(RoomPaymentType::getPaymentTypeId).toList();
        if(CollectionUtils.isNotEmpty(list)){
            List<PaymentType> paymentTypeList = paymentTypeService.lambdaQuery().in(PaymentType::getId, list).list();
            return Result.ok(paymentTypeList);
        }
        return Result.ok();
    }

    @Operation(summary = "获取全部支付方式列表")
    @GetMapping("list")
    public Result<List<PaymentType>> list() {
        return Result.ok(paymentTypeService.list());
    }
}
