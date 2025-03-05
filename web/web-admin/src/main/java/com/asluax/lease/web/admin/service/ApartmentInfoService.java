package com.asluax.lease.web.admin.service;

import com.asluax.lease.model.entity.ApartmentInfo;
import com.asluax.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.asluax.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface ApartmentInfoService extends IService<ApartmentInfo> {

    List<ApartmentItemVo> getVoList(List<ApartmentInfo> apartmentInfoList);

    List<ApartmentInfo> getApartmentByQueryVo(ApartmentQueryVo queryVo);
}
