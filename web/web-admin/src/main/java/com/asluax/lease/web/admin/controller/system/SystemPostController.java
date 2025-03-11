package com.asluax.lease.web.admin.controller.system;

import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.SystemPost;
import com.asluax.lease.model.enums.BaseStatus;
import com.asluax.lease.web.admin.service.SystemPostService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "后台用户岗位管理")
@RequestMapping("/admin/system/post")
public class SystemPostController {

    @Autowired
    SystemPostService systemPostService;

    @Operation(summary = "分页获取岗位信息")
    @GetMapping("page")
    private Result<IPage<SystemPost>> page(@RequestParam long current, @RequestParam long size) {
        Page<SystemPost> page = new Page<>(current, size);
        return Result.ok(systemPostService.page(page));
    }

    @Operation(summary = "保存或更新岗位信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemPost systemPost) {
        return Result.ok(systemPostService.saveOrUpdate(systemPost));
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据id删除岗位")
    public Result removeById(@RequestParam Long id) {
        return Result.ok(systemPostService.removeById(id));
    }

    @GetMapping("getById")
    @Operation(summary = "根据id获取岗位信息")
    public Result<SystemPost> getById(@RequestParam Long id) {
        return Result.ok(systemPostService.getById(id));
    }

    @Operation(summary = "获取全部岗位列表")
    @GetMapping("list")
    public Result<List<SystemPost>> list() {
        return Result.ok(systemPostService.list());
    }

    @Operation(summary = "根据岗位id修改状态")
    @PostMapping("updateStatusByPostId")
    public Result updateStatusByPostId(@RequestParam Long id, @RequestParam BaseStatus status) {
        SystemPost systemPost = new SystemPost();
        systemPost.setStatus(status);
        systemPost.setId(id);
        return Result.ok(systemPostService.updateById(systemPost));
    }
}
