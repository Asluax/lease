package com.asluax.lease.web.app.mapper;

import com.asluax.lease.model.entity.RoomInfo;
import com.asluax.lease.web.app.vo.room.RoomItemVo;
import com.asluax.lease.web.app.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Mapper
 * @createDate 2023-07-26 11:12:39
 * @Entity com.asluax.lease.model.entity.RoomInfo
 */
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {


    IPage<RoomItemVo> pageItem(@Param("page") IPage<RoomItemVo> page, @Param("queryVo") RoomQueryVo queryVo);

    RoomInfo getDetailById(Long id);

    IPage<RoomItemVo> pageItemByApartmentId(@Param("page") IPage<RoomItemVo> page, @Param("id") Long id);
}