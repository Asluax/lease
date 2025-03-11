package com.asluax.lease.web.admin.service;

import com.asluax.lease.web.admin.vo.login.CaptchaVo;
import com.asluax.lease.web.admin.vo.login.LoginVo;

public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);
}
