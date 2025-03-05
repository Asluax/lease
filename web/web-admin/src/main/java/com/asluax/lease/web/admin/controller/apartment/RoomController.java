package com.asluax.lease.web.admin.controller.apartment;


import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.RoomInfo;
import com.asluax.lease.model.enums.ReleaseStatus;
import com.asluax.lease.web.admin.service.RoomInfoService;
import com.asluax.lease.web.admin.utils.GetPages;
import com.asluax.lease.web.admin.vo.room.RoomDetailVo;
import com.asluax.lease.web.admin.vo.room.RoomItemVo;
import com.asluax.lease.web.admin.vo.room.RoomQueryVo;
import com.asluax.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "房间信息管理")
@RestController
@RequestMapping("/admin/room")
public class RoomController {

    @Autowired
    RoomInfoService roomInfoService;

    @Operation(summary = "保存或更新房间信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody RoomSubmitVo roomSubmitVo) {
        roomInfoService.saveOrUpdateByVo(roomSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询房间列表")
    @GetMapping("pageItem")
    public Result<IPage<RoomInfo>> pageItem(@RequestParam long current, @RequestParam long size, RoomQueryVo queryVo) {
        List<RoomInfo> roomInfoList = roomInfoService.getListByQueryVo(queryVo);
        List<RoomItemVo> roomItemVos = roomInfoService.getVoList(roomInfoList);
        IPage<RoomInfo> iPage = GetPages.getPageFromList(roomInfoList, current, size);
        return Result.ok(iPage);
    }

    @Operation(summary = "根据id获取房间详细信息")
    @GetMapping("getDetailById")
    public Result<RoomDetailVo> getDetailById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据id删除房间信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据id修改房间发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(Long id, ReleaseStatus status) {
        return Result.ok();
    }

    @GetMapping("listBasicByApartmentId")
    @Operation(summary = "根据公寓id查询房间列表")
    public Result<List<RoomInfo>> listBasicByApartmentId(Long id) {
        return Result.ok();
    }

}


















