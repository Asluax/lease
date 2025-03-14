package com.asluax.lease.web.app.service;

import com.asluax.lease.model.entity.LabelInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【label_info(标签信息表)】的数据库操作Service
 * @createDate 2023-07-26 11:12:39
 */
public interface LabelInfoService extends IService<LabelInfo> {

    List<LabelInfo> getListByRoomId(Long id);
}
