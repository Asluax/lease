package com.asluax.lease.web.app.mapper;

import com.asluax.lease.model.entity.ApartmentInfo;
import com.asluax.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Mapper
 * @createDate 2023-07-26 11:12:39
 * @Entity com.asluax.lease.model.entity.ApartmentInfo
 */
public interface ApartmentInfoMapper extends BaseMapper<ApartmentInfo> {


    ApartmentDetailVo getDetailById(Long id);
}




