package com.asluax.lease.web.app.service;

import com.asluax.lease.model.entity.GraphInfo;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.web.app.vo.graph.GraphVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【graph_info(图片信息表)】的数据库操作Service
 * @createDate 2023-07-26 11:12:39
 */
public interface GraphInfoService extends IService<GraphInfo> {

    List<GraphVo> getListByItemTypeAndId(ItemType itemType, Long id);
}
