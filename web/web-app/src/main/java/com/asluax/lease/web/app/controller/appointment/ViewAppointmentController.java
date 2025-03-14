package com.asluax.lease.web.app.controller.appointment;


import com.asluax.lease.common.constant.LoginUserApp;
import com.asluax.lease.common.constant.LoginUserContext;
import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.ViewAppointment;
import com.asluax.lease.web.app.service.ViewAppointmentService;
import com.asluax.lease.web.app.vo.appointment.AppointmentDetailVo;
import com.asluax.lease.web.app.vo.appointment.AppointmentItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "看房预约信息")
@RestController
@RequestMapping("/app/appointment")
public class ViewAppointmentController {

    @Autowired
    ViewAppointmentService viewAppointmentService;

    @Operation(summary = "保存或更新看房预约")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ViewAppointment viewAppointment) {
        viewAppointment.setUserId(LoginUserContext.getLoginUser().getUserId());
        return Result.ok(viewAppointmentService.saveOrUpdate(viewAppointment));
    }

    @Operation(summary = "查询个人预约看房列表")
    @GetMapping("listItem")
    public Result<List<AppointmentItemVo>> listItem() {
        LoginUserApp loginUserApp = LoginUserContext.getLoginUserApp();
        return Result.ok(viewAppointmentService.lisItem(loginUserApp.getUserId()));
    }


    @GetMapping("getDetailById")
    @Operation(summary = "根据ID查询预约详情信息")
    public Result<AppointmentDetailVo> getDetailById(Long id) {
        return Result.ok(viewAppointmentService.getDetailById(id));
    }

}

