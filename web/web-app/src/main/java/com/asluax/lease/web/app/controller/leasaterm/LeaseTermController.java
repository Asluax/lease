package com.asluax.lease.web.app.controller.leasaterm;

import com.asluax.lease.common.result.Result;
import com.asluax.lease.model.entity.LeaseTerm;
import com.asluax.lease.model.entity.RoomLabel;
import com.asluax.lease.web.app.service.LeaseTermService;
import com.asluax.lease.web.app.service.RoomLabelService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/term/")
@Tag(name = "租期信息")
public class LeaseTermController {

    @Autowired
    RoomLabelService roomLabelService;
    @Autowired
    LeaseTermService leaseTermService;

    @GetMapping("listByRoomId")
    @Operation(summary = "根据房间id获取可选获取租期列表")
    public Result<List<LeaseTerm>> list(@RequestParam Long id) {
        List<RoomLabel> roomLabelList = roomLabelService.lambdaQuery().eq(RoomLabel::getRoomId, id).list();
        List<Long> list = roomLabelList.stream().map(RoomLabel::getLabelId).toList();
        if (CollectionUtils.isNotEmpty(list)) {
            List<LeaseTerm> leaseTermList = leaseTermService.lambdaQuery().in(LeaseTerm::getId, list).list();
            return Result.ok(leaseTermList);
        }
        return Result.ok();
    }
}
