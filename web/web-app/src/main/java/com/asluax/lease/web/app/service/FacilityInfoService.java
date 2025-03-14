package com.asluax.lease.web.app.service;

import com.asluax.lease.model.entity.FacilityInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【facility_info(配套信息表)】的数据库操作Service
 * @createDate 2023-07-26 11:12:39
 */
public interface FacilityInfoService extends IService<FacilityInfo> {

    List<FacilityInfo> getListByRoomId(Long id);
}
