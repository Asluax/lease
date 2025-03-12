package com.asluax.lease.web.app.controller.agreement;


import com.asluax.lease.common.constant.LoginUserApp;
import com.asluax.lease.common.constant.LoginUserContext;
import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.LeaseAgreement;
import com.asluax.lease.model.enums.LeaseStatus;
import com.asluax.lease.web.app.service.LeaseAgreementService;
import com.asluax.lease.web.app.vo.agreement.AgreementDetailVo;
import com.asluax.lease.web.app.vo.agreement.AgreementItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/agreement")
@Tag(name = "租约信息")
public class LeaseAgreementController {

    @Autowired
    LeaseAgreementService leaseAgreementService;

    @Operation(summary = "获取个人租约基本信息列表")
    @GetMapping("listItem")
    public Result<List<AgreementItemVo>> listItem() {
        LoginUserApp loginUserApp = LoginUserContext.getLoginUserApp();
        return Result.ok(leaseAgreementService.listItem(loginUserApp.getPhone()));
    }

    @Operation(summary = "根据id获取租约详细信息")
    @GetMapping("getDetailById")
    public Result<AgreementDetailVo> getDetailById(@RequestParam Long id) {
        return Result.ok(leaseAgreementService.getDetailById(id));
    }

    @Operation(summary = "根据id更新租约状态", description = "用于确认租约和提前退租")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus leaseStatus) {
        LeaseAgreement leaseAgreement = leaseAgreementService.getById(id);
        leaseAgreement.setStatus(leaseStatus);
        leaseAgreementService.updateById(leaseAgreement);
        return Result.ok();
    }

    @Operation(summary = "保存或更新租约", description = "用于续约")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        leaseAgreementService.saveOrUpdate(leaseAgreement);
        return Result.ok();
    }
}