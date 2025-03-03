package com.asluax.lease.web.admin.controller.apartment;


import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.LabelInfo;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.web.admin.service.LabelInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//*接口功能已实现
@Tag(name = "标签管理")
@RestController
@RequestMapping("/admin/label")
public class LabelController {

    @Autowired
    LabelInfoService labelInfoService;

    @Operation(summary = "（根据类型）查询标签列表")
    @GetMapping("list")
    public Result<List<LabelInfo>> labelList(@RequestParam(required = false) ItemType type) {
        if (type==null){
            return Result.ok(labelInfoService.list());
        }
        return Result.ok(labelInfoService.lambdaQuery().eq(LabelInfo::getType, type).list());
    }

    @Operation(summary = "新增或修改标签信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdateLabel(@RequestBody LabelInfo labelInfo) {
        return Result.ok(labelInfoService.saveOrUpdate(labelInfo));
    }

    @Operation(summary = "根据id删除标签信息")
    @DeleteMapping("deleteById")
    public Result deleteLabelById(@RequestParam Long id) {
        return Result.ok(labelInfoService.removeById(id));
    }
}
