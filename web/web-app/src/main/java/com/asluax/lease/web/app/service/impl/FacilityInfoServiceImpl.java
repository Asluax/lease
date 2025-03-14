package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.model.entity.FacilityInfo;
import com.asluax.lease.web.app.mapper.FacilityInfoMapper;
import com.asluax.lease.web.app.service.FacilityInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【facility_info(配套信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class FacilityInfoServiceImpl extends ServiceImpl<FacilityInfoMapper, FacilityInfo>
        implements FacilityInfoService {


    @Override
    public List<FacilityInfo> getListByRoomId(Long id) {
        return baseMapper.getListByRoomId(id);
    }
}




