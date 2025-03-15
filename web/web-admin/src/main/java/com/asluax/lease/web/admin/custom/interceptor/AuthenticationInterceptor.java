package com.asluax.lease.web.admin.custom.interceptor;

import com.asluax.lease.common.constant.LoginUser;
import com.asluax.lease.common.constant.LoginUserContext;
import com.asluax.lease.common.exception.MyException;
import com.asluax.lease.common.result.ResultCodeEnum;
import com.asluax.lease.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("access_token");
        if (token == null) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                System.out.println(headerName + request.getHeader(headerName));
            }
            throw new MyException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        } else {
            Claims claims = JwtUtil.parseToken(token);
            Long userid =  claims.get("userId",Long.class);
            String username = claims.get("username", String.class);
            LoginUserContext.setLoginUser(new LoginUser(userid,username));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserContext.clear();
    }
}
