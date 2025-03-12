package com.asluax.lease.web.app.custom.interceptor;

import com.asluax.lease.common.constant.LoginUserApp;
import com.asluax.lease.common.constant.LoginUserContext;
import com.asluax.lease.common.exception.MyException;
import com.asluax.lease.common.result.ResultCodeEnum;
import com.asluax.lease.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("access_token");

        if (token == null) {
            throw new MyException(ResultCodeEnum.APP_LOGIN_AUTH);
        } else {
            Claims claims = JwtUtil.parseToken(token);
            Long userId = claims.get("userId", Long.class);
            String phone = claims.get("phone", String.class);
            LoginUserContext.setLoginUserApp(new LoginUserApp(userId, phone));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserContext.clearApp();
    }
}