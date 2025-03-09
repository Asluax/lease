package com.asluax.lease.web.admin.custom.config.mybatisplus;

import com.asluax.lease.common.mybatisplus.MybatisPlusConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MybatisPlusConfiguration.class)  // 手动导入 common 的配置
@MapperScan("com.asluax.lease.web.admin.mapper")
public class MybatisPlusConfig {
}
