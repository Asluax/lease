package com.asluax.lease.web.admin.controller.apartment;


import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.FeeKey;
import com.asluax.lease.model.entity.FeeValue;
import com.asluax.lease.web.admin.service.FeeKeyService;
import com.asluax.lease.web.admin.service.FeeValueService;
import com.asluax.lease.web.admin.vo.fee.FeeKeyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "房间杂费管理")
@RestController
@RequestMapping("/admin/fee")
public class FeeController {

    @Autowired
    FeeKeyService feeKeyService;
    @Autowired
    FeeValueService feeValueService;

    @Operation(summary = "保存或更新杂费名称")
    @PostMapping("key/saveOrUpdate")
    public Result saveOrUpdateFeeKey(@RequestBody FeeKey feeKey) {
        return Result.ok(feeKeyService.saveOrUpdate(feeKey));
    }

    @Operation(summary = "保存或更新杂费值")
    @PostMapping("value/saveOrUpdate")
    public Result saveOrUpdateFeeValue(@RequestBody FeeValue feeValue) {
        return Result.ok(feeValueService.saveOrUpdate(feeValue));
    }


    @Operation(summary = "查询全部杂费名称和杂费值列表")
    @GetMapping("list")
    public Result<List<FeeKeyVo>> feeInfoList() {
        return Result.ok(feeKeyService.getFeeList());
    }

    @Operation(summary = "根据id删除杂费名称")
    @DeleteMapping("key/deleteById")
    public Result deleteFeeKeyById(@RequestParam Long feeKeyId) {
        LambdaQueryWrapper<FeeValue> eq = new LambdaQueryWrapper<FeeValue>().eq(FeeValue::getFeeKeyId, feeKeyId);
        feeValueService.remove(eq);
        return Result.ok(feeKeyService.removeById(feeKeyId));
    }

    @Operation(summary = "根据id删除杂费值")
    @DeleteMapping("value/deleteById")
    public Result deleteFeeValueById(@RequestParam Long id) {
        return Result.ok(feeValueService.removeById(id));
    }
}
