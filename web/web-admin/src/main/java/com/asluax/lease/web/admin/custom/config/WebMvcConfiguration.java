package com.asluax.lease.web.admin.custom.config;

import com.asluax.lease.web.admin.custom.config.converter.StringToBaseEnumConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//*SpringMVC配置
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
//    @Autowired
//    private StringToItemTypeConverter stringToItemTypeConverter;
//
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(this.stringToItemTypeConverter);
//    }

    @Autowired
    private StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(this.stringToBaseEnumConverterFactory);
    }
}
