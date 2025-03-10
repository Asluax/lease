package com.asluax.lease.web.admin.controller.lease;


import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.ViewAppointment;
import com.asluax.lease.model.enums.AppointmentStatus;
import com.asluax.lease.web.admin.service.ViewAppointmentService;
import com.asluax.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.asluax.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "预约看房管理")
@RequestMapping("/admin/appointment")
@RestController
public class ViewAppointmentController {

    @Autowired
    ViewAppointmentService viewAppointmentService;

    @Operation(summary = "分页查询预约信息")
    @GetMapping("page")
    public Result<IPage<AppointmentVo>> page(@RequestParam long current, @RequestParam long size, AppointmentQueryVo queryVo) {
        IPage<AppointmentVo> iPage = new Page<>(current, size);
        return Result.ok(viewAppointmentService.pageAppointmentByQuery(iPage, queryVo));
    }

    @Operation(summary = "根据id更新预约状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        LambdaUpdateWrapper<ViewAppointment> viewAppointmentLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        viewAppointmentLambdaUpdateWrapper.eq(ViewAppointment::getId, id);
        viewAppointmentLambdaUpdateWrapper.set(ViewAppointment::getAppointmentStatus, status);
        return Result.ok(viewAppointmentService.update(viewAppointmentLambdaUpdateWrapper));
    }

}
