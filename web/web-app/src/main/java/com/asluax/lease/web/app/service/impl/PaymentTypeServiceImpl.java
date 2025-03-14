package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.model.entity.PaymentType;
import com.asluax.lease.web.app.mapper.PaymentTypeMapper;
import com.asluax.lease.web.app.service.PaymentTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【payment_type(支付方式表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class PaymentTypeServiceImpl extends ServiceImpl<PaymentTypeMapper, PaymentType>
        implements PaymentTypeService {

    @Override
    public List<PaymentType> getListByRoomId(Long id) {
        return baseMapper.getListByRoomId(id);
    }
}




