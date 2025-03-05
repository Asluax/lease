package com.asluax.lease.web.admin.controller.apartment;


import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.AttrKey;
import com.asluax.lease.model.entity.AttrValue;
import com.asluax.lease.web.admin.service.AttrKeyService;
import com.asluax.lease.web.admin.service.AttrValueService;
import com.asluax.lease.web.admin.vo.attr.AttrKeyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "房间属性管理")
@RestController
@RequestMapping("/admin/attr")
public class AttrController {

    @Autowired
    AttrKeyService attrKeyService;
    @Autowired
    AttrValueService attrValueService;

    @Operation(summary = "新增或更新属性名称")
    @PostMapping("key/saveOrUpdate")
    public Result saveOrUpdateAttrKey(@RequestBody AttrKey attrKey) {
        return Result.ok(attrKeyService.saveOrUpdate(attrKey));
    }

    @Operation(summary = "新增或更新属性值")
    @PostMapping("value/saveOrUpdate")
    public Result saveOrUpdateAttrValue(@RequestBody AttrValue attrValue) {
        return Result.ok(attrValueService.saveOrUpdate(attrValue));
    }


    @Operation(summary = "查询全部属性名称和属性值列表")
    @GetMapping("list")
    public Result<List<AttrKeyVo>> listAttrInfo() {
        List<AttrKey> keys = attrKeyService.list();
        List<AttrValue> values = attrValueService.list();
        ArrayList<AttrKeyVo> vos = attrKeyService.getAttrKeyVos(keys, values);
        return Result.ok(vos);
    }

    @Operation(summary = "根据id删除属性名称")
    @DeleteMapping("key/deleteById")
    public Result removeAttrKeyById(@RequestParam Long attrKeyId) {
        LambdaQueryWrapper<AttrValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttrValue::getAttrKeyId,attrKeyId);
        attrValueService.remove(queryWrapper);
        return Result.ok(attrKeyService.removeById(attrKeyId));
    }

    @Operation(summary = "根据id删除属性值")
    @DeleteMapping("value/deleteById")
    public Result removeAttrValueById(@RequestParam Long id) {
        return Result.ok(attrValueService.removeById(id));
    }

}
