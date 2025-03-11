package com.asluax.lease.web.admin.controller.user;


import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.UserInfo;
import com.asluax.lease.model.enums.BaseStatus;
import com.asluax.lease.web.admin.service.UserInfoService;
import com.asluax.lease.web.admin.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/admin/user")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @Operation(summary = "分页查询用户信息")
    @GetMapping("page")
    public Result<IPage<UserInfo>> pageUserInfo(@RequestParam long current, @RequestParam long size, UserInfoQueryVo queryVo) {
        Page<UserInfo> page = new Page<>(current, size);
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        if(queryVo.getPhone()!=null){
            queryWrapper.like(UserInfo::getPhone,queryVo.getPhone());
        }
        if(queryVo.getStatus()!=null){
            queryWrapper.like(UserInfo::getStatus,queryVo.getStatus());
        }
        return Result.ok(userInfoService.page(page,queryWrapper));
    }

    @Operation(summary = "根据用户id更新账号状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setStatus(status);
        userInfo.setId(id);
        return Result.ok(userInfoService.updateById(userInfo));
    }
}
