package com.asluax.lease.web.admin.mapper;


import com.asluax.lease.model.entity.RoomInfo;
import com.asluax.lease.web.admin.vo.room.RoomItemVo;
import com.asluax.lease.web.admin.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.asluax.lease.model.RoomInfo
 */
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {

    IPage<RoomItemVo> getRoomInfoByPageItem(@Param("iPage") IPage<RoomItemVo> iPage, @Param("queryVo") RoomQueryVo queryVo);
}




