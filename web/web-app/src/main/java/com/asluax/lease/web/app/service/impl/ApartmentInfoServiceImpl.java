package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.model.entity.ApartmentInfo;
import com.asluax.lease.web.app.mapper.*;
import com.asluax.lease.web.app.service.ApartmentInfoService;
import com.asluax.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {


    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        return baseMapper.getDetailById(id);
    }
}




