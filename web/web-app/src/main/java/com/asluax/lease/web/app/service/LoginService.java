package com.asluax.lease.web.app.service;

import com.asluax.lease.web.app.vo.user.LoginVo;

public interface LoginService {


    String login(LoginVo loginVo);

    void getCode(String phone);

}
