package com.asluax.lease.web.app.controller.appointment;


import com.asluax.lease.common.constant.LoginUserApp;
import com.asluax.lease.common.constant.LoginUserContext;
import com.asluax.lease.common.result.Result;
import com.asluax.lease.common.utils.CopyUtil;
import com.asluax.lease.model.entity.GraphInfo;
import com.asluax.lease.model.entity.ViewAppointment;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.web.app.service.ApartmentInfoService;
import com.asluax.lease.web.app.service.GraphInfoService;
import com.asluax.lease.web.app.service.ViewAppointmentService;
import com.asluax.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.asluax.lease.web.app.vo.apartment.ApartmentItemVo;
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
    @Autowired
    ApartmentInfoService apartmentInfoService;
    @Autowired
    GraphInfoService graphInfoService;

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
        ViewAppointment appointment = viewAppointmentService.getById(id);
        AppointmentDetailVo appointmentDetailVo = CopyUtil.copyProperties(appointment, AppointmentDetailVo.class);
        ApartmentDetailVo apartmentDetailVo = apartmentInfoService.getDetailById(appointmentDetailVo.getApartmentId());
        ApartmentItemVo apartmentItemVo = CopyUtil.copyProperties(apartmentDetailVo, ApartmentItemVo.class);
        List<GraphInfo> graphInfos = graphInfoService.lambdaQuery()
                .eq(GraphInfo::getItemId, appointmentDetailVo.getApartmentId())
                .eq(GraphInfo::getItemType, ItemType.APARTMENT).list();
        apartmentItemVo.setGraphVoList(graphInfos);
        appointmentDetailVo.setApartmentItemVo(apartmentItemVo);
        return Result.ok(appointmentDetailVo);
    }

}

