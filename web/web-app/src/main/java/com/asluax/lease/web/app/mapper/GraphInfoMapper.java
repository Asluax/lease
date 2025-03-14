package com.asluax.lease.web.app.mapper;

import com.asluax.lease.model.entity.GraphInfo;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.web.app.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【graph_info(图片信息表)】的数据库操作Mapper
 * @createDate 2023-07-26 11:12:39
 * @Entity com.asluax.lease.model.entity.GraphInfo
 */
public interface GraphInfoMapper extends BaseMapper<GraphInfo> {

    List<GraphVo> getListByItemTypeAndId(@Param("itemType") ItemType itemType, @Param("id") Long id);
}




