package com.asluax.lease.web.app.controller.login;


import com.asluax.lease.common.constant.LoginUserApp;
import com.asluax.lease.common.constant.LoginUserContext;
import com.asluax.lease.common.exception.MyException;
import com.asluax.lease.common.result.Result;
import com.asluax.lease.common.result.ResultCodeEnum;
import com.asluax.lease.model.entity.UserInfo;
import com.asluax.lease.web.app.service.LoginService;
import com.asluax.lease.web.app.service.UserInfoService;
import com.asluax.lease.web.app.vo.user.LoginVo;
import com.asluax.lease.web.app.vo.user.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Tag(name = "登录管理")
@RequestMapping("/app/")
public class LoginController {

    @Autowired
    LoginService loginService;
    @Autowired
    UserInfoService userInfoService;

    @GetMapping("login/getCode")
    @Operation(summary = "获取短信验证码")
    public Result getCode(@RequestParam String phone) {
        loginService.getCode(phone);
        return Result.ok();
    }

    @PostMapping("login")
    @Operation(summary = "登录")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        return Result.ok(loginService.login(loginVo));
    }

    @GetMapping("info")
    @Operation(summary = "获取登录用户信息")
    public Result<UserInfoVo> info() {
        LoginUserApp loginUser = LoginUserContext.getLoginUserApp();
        if (!Objects.isNull(loginUser)) {
            UserInfo user = userInfoService.getById(loginUser.getUserId());
            UserInfoVo userInfoVo = new UserInfoVo(user.getNickname(), user.getAvatarUrl());
            return Result.ok(userInfoVo);
        }
        throw new MyException(ResultCodeEnum.APP_LOGIN_AUTH);
    }
}
