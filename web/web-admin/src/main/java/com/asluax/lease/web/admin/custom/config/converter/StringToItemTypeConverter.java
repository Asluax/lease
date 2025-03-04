package com.asluax.lease.web.admin.custom.config.converter;

import com.asluax.lease.model.enums.ItemType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

//*将请求参数String转为ItemType枚举
@Component
public class StringToItemTypeConverter implements Converter<String,ItemType> {
        @Override
        public ItemType convert(String code) {
            for (ItemType value : ItemType.values()) {
                if (value.getCode().equals(Integer.valueOf(code))) {
                    return value;
                }
            }
            throw new IllegalArgumentException("code非法");
        }
}
