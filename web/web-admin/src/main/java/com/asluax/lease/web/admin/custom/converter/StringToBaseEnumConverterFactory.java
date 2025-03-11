package com.asluax.lease.web.admin.custom.converter;

import com.asluax.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;


//*全局转换枚举类，要求枚举类实现BaseEnum接口
@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> {
            //*Class.getEnumConstants() 方法是 Java 反射 API 中的一个方法，用于获取表示枚举类型的 Class 对象中所有枚举常量的数组
            for (T enumConstant : targetType.getEnumConstants()) {
                if (enumConstant.getCode().equals(Integer.valueOf(source))) {
                    return enumConstant;
                }
            }
            throw new IllegalArgumentException("非法的枚举值:" + source);
        };
    }
}
