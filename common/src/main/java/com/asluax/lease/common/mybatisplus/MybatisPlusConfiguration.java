package com.asluax.lease.common.mybatisplus;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.asluax.lease.web.*.mapper")
public class MybatisPlusConfiguration {
}