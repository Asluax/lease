package com.asluax.lease.web.admin.controller.system;


import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.SystemUser;
import com.asluax.lease.model.enums.BaseStatus;
import com.asluax.lease.web.admin.service.SystemUserService;
import com.asluax.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.asluax.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user")
public class SystemUserController {

    @Autowired
    SystemUserService systemUserService;

    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page")
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size, SystemUserQueryVo queryVo) {
        IPage<SystemUserItemVo> iPage = new Page<>(current,size);
        return Result.ok(systemUserService.getSystemUserItemVoPage(iPage,queryVo));
    }

    @Operation(summary = "根据ID查询后台用户信息")
    @GetMapping("getById")
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
        return Result.ok(systemUserService.getByIdForVo(id));
    }

    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
        String password = systemUser.getPassword();
        String dpassword = DigestUtils.md5Hex(password);
        systemUser.setPassword(dpassword);
        return Result.ok(systemUserService.saveOrUpdate(systemUser));
    }

    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable")
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        return Result.ok(!systemUserService.lambdaQuery().eq(SystemUser::getUsername, username).exists());
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        return Result.ok(systemUserService.removeById(id));
    }

    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId")
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        SystemUser systemUser = new SystemUser();
        systemUser.setId(id);
        systemUser.setStatus(status);
        return Result.ok(systemUserService.updateById(systemUser));
    }
}
