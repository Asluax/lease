package com.asluax.lease.web.app.service.impl;


import com.asluax.lease.common.constant.LoginUser;
import com.asluax.lease.common.constant.LoginUserContext;
import com.asluax.lease.common.exception.MyException;
import com.asluax.lease.common.result.ResultCodeEnum;
import com.asluax.lease.common.utils.JwtUtil;
import com.asluax.lease.model.entity.UserInfo;
import com.asluax.lease.model.enums.BaseStatus;
import com.asluax.lease.web.app.service.LoginService;
import com.asluax.lease.web.app.service.UserInfoService;
import com.asluax.lease.web.app.utils.HttpUtils;
import com.asluax.lease.web.app.vo.user.LoginVo;
import com.asluax.lease.web.app.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserInfoService userInfoService;

    @Override
    public String login(LoginVo loginVo) {
        String code = loginVo.getCode();
        if (StringUtils.isEmpty(code)) {
            throw new MyException(ResultCodeEnum.APP_LOGIN_CODE_EMPTY);
        }
        String phone = loginVo.getPhone();
        if (StringUtils.isEmpty(phone)) {
            throw new MyException(ResultCodeEnum.APP_LOGIN_PHONE_EMPTY);
        }
        String luaScript = "local value = redis.call(\"get\",KEYS[1]) if KEYS[1] ~= nil then redis.call(\"del\",KEYS[1]) end return value ";//redis.call("del",KEYS[1])
        // 脚本
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(String.class);
        redisScript.setScriptText(luaScript);
        // 执行
        String result = redisTemplate.execute(redisScript, Collections.singletonList(phone));
        if (StringUtils.isEmpty(result)) {
            throw new MyException(ResultCodeEnum.APP_LOGIN_CODE_EXPIRED);
        }
        if (!result.equals(code)) {
            throw new MyException(ResultCodeEnum.APP_LOGIN_CODE_ERROR);
        }
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getPhone, phone).one();
        if (userInfo.getStatus().equals(BaseStatus.DISABLE)) {
            throw new MyException(ResultCodeEnum.APP_ACCOUNT_DISABLED_ERROR);
        }
        return JwtUtil.createToken(userInfo.getId(), userInfo.getPhone());
    }

    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 生成 100000-999999 之间的随机数
        return String.valueOf(otp);
    }

    @Override
    public void getCode(String phone) {
        String text = generateOTP();
        redisTemplate.opsForValue().set(phone,text,3, TimeUnit.MINUTES);
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "43a4db7dafd54d7bb919b479111712b3";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+text+",**minute**:3");

//smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html

        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
                /*
                  重要提示如下:
                  HttpUtils请从https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java下载

                  相应的依赖请参照
                  https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                 */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserInfoVo info() {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        if(!Objects.isNull(loginUser)){
            UserInfo userInfo = userInfoService.getById(loginUser.getUserId());
            return new UserInfoVo(userInfo.getNickname(),userInfo.getAvatarUrl());
        }
        throw new MyException(ResultCodeEnum.APP_LOGIN_AUTH);
    }
}
