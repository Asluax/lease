package com.asluax.lease.web.admin.controller.login;


import com.asluax.lease.common.constant.LoginUser;
import com.asluax.lease.common.constant.LoginUserContext;
import com.asluax.lease.common.result.Result;
import com.asluax.lease.common.utils.CopyUtil;
import com.asluax.lease.model.entity.SystemUser;
import com.asluax.lease.web.admin.service.LoginService;
import com.asluax.lease.web.admin.service.SystemUserService;
import com.asluax.lease.web.admin.vo.login.CaptchaVo;
import com.asluax.lease.web.admin.vo.login.LoginVo;
import com.asluax.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    LoginService loginService;
    @Autowired
    SystemUserService systemUserService;

    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha")
    public Result<CaptchaVo> getCaptcha() {
        return Result.ok(loginService.getCaptcha());
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        return Result.ok(loginService.login(loginVo));
    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    public Result<SystemUserInfoVo> info() {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        SystemUser user = systemUserService.getById(loginUser.getUserId());
        return Result.ok(CopyUtil.copyProperties(user,SystemUserInfoVo.class));
    }
}