package com.asluax.lease.web.admin.service;

import com.asluax.lease.model.entity.RoomInfo;
import com.asluax.lease.web.admin.vo.room.RoomDetailVo;
import com.asluax.lease.web.admin.vo.room.RoomItemVo;
import com.asluax.lease.web.admin.vo.room.RoomQueryVo;
import com.asluax.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface RoomInfoService extends IService<RoomInfo> {

    void saveOrUpdateByVo(RoomSubmitVo roomSubmitVo);

    List<RoomInfo> getListByQueryVo(RoomQueryVo queryVo);

    List<RoomItemVo> getVoList(List<RoomInfo> roomInfoList);

    IPage<RoomItemVo> getRoomInfoByPageItem(IPage<RoomItemVo> iPage, RoomQueryVo queryVo);

    RoomDetailVo getDetailById(Long id);
}
