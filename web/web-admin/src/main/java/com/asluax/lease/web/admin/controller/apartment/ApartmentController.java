package com.asluax.lease.web.admin.controller.apartment;


import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.ApartmentInfo;
import com.asluax.lease.model.enums.ReleaseStatus;
import com.asluax.lease.web.admin.service.ApartmentInfoService;
import com.asluax.lease.web.admin.service.RoomInfoService;
import com.asluax.lease.web.admin.utils.GetPages;
import com.asluax.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.asluax.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.asluax.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.asluax.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "公寓信息管理")
@RestController
@RequestMapping("/admin/apartment")
public class ApartmentController {

    @Autowired
    ApartmentInfoService apartmentInfoService;
    @Autowired
    RoomInfoService roomInfoService;

    @Operation(summary = "保存或更新公寓信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ApartmentSubmitVo apartmentSubmitVo) {
        ApartmentInfo apartmentInfo = new ApartmentInfo();
        BeanUtils.copyProperties(apartmentSubmitVo, apartmentInfo);
        return Result.ok(apartmentInfoService.saveOrUpdate(apartmentInfo));
    }

    //*分析，返回结果需要包含房间总数和空闲房间数，这两参数需要查询room_info，并且包含分页查询和条件查询(省份ID，城市ID，区域ID)
    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("pageItem")
    public Result<IPage<ApartmentItemVo>> pageItem(@RequestParam long current, @RequestParam long size, ApartmentQueryVo queryVo) {
        //*查询所有符合条件的公寓
        List<ApartmentInfo> apartmentInfoList = apartmentInfoService.getApartmentByQueryVo(queryVo);
        //*根据查询的公寓ID查询所有公寓的房间ID,一次遍历找到所有结果集
        List<ApartmentItemVo> list = apartmentInfoService.getVoList(apartmentInfoList);
        //* 自定义分页
        IPage<ApartmentItemVo> iPage = GetPages.getPageFromList(list, current, size);
        return Result.ok(iPage);
    }

    @NotNull


    @Operation(summary = "根据ID获取公寓详细信息")
    @GetMapping("getDetailById")
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        ApartmentInfo info = apartmentInfoService.getById(id);
        ApartmentDetailVo vo = new ApartmentDetailVo();
        BeanUtils.copyProperties(info, vo);
        return Result.ok(vo);
    }

    @Operation(summary = "根据id删除公寓信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {




        return Result.ok(apartmentInfoService.removeById(id));
    }

    @Operation(summary = "根据id修改公寓发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        ApartmentInfo apartmentInfo = new ApartmentInfo();
        apartmentInfo.setId(id);
        apartmentInfo.setIsRelease(status);
        return Result.ok(apartmentInfoService.updateById(apartmentInfo));
    }

    @Operation(summary = "根据区县id查询公寓信息列表")
    @GetMapping("listInfoByDistrictId")
    public Result<List<ApartmentInfo>> listInfoByDistrictId(@RequestParam Long id) {
        List<ApartmentInfo> list = apartmentInfoService.lambdaQuery().eq(ApartmentInfo::getDistrictId, id).list();
        return Result.ok(list);
    }
}














