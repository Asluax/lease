package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.common.constant.RedisConstant;
import com.asluax.lease.common.exception.MyException;
import com.asluax.lease.common.result.ResultCodeEnum;
import com.asluax.lease.common.utils.JwtUtil;
import com.asluax.lease.model.entity.SystemUser;
import com.asluax.lease.web.admin.service.LoginService;
import com.asluax.lease.web.admin.service.SystemUserService;
import com.asluax.lease.web.admin.vo.login.CaptchaVo;
import com.asluax.lease.web.admin.vo.login.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wf.captcha.SpecCaptcha;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    SystemUserService systemUserService;

    @Override
    public CaptchaVo getCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String img = specCaptcha.toBase64();
        String text = specCaptcha.text().toLowerCase();
        String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(key, text, 1, TimeUnit.MINUTES);
        return new CaptchaVo(img, key);
    }

    @Override
    public String login(LoginVo loginVo) {
        String captchaCode = loginVo.getCaptchaCode();
        if (StringUtils.isEmpty(captchaCode)) {
            throw new MyException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }
        String captchaKey = loginVo.getCaptchaKey();
        String luaScript = "local value = redis.call(\"get\",KEYS[1]) if KEYS[1] ~= nil then redis.call(\"del\",KEYS[1]) end return value ";//redis.call("del",KEYS[1])
        // 脚本
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(String.class);
        redisScript.setScriptText(luaScript);
        // 执行
        String result = redisTemplate.execute(redisScript, Collections.singletonList(captchaKey));
        //*查询结果为空
        if(StringUtils.isEmpty(result)){
            throw new MyException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }
        //* 缓存查询结果与前端传入参数不同
        if(!result.equals(captchaCode)){
            throw new MyException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }
        LambdaQueryWrapper<SystemUser> lambdaQueryWrapper = new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUsername, loginVo.getUsername());
        SystemUser user = systemUserService.getOne(lambdaQueryWrapper);
        //* 数据库查询结果username与前端传入参数不同
        if(Objects.isNull(user)){
            throw new MyException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }
        String dpassword = DigestUtils.md5Hex(loginVo.getPassword());
        //* 数据库查询结果password与前端传入参数不同
        if(!user.getPassword().equals(dpassword)){
            throw new MyException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }
        return JwtUtil.createToken(user.getId(), user.getName());
    }
}
