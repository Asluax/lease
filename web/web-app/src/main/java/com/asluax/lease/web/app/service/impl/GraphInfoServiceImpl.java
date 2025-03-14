package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.model.entity.GraphInfo;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.web.app.mapper.GraphInfoMapper;
import com.asluax.lease.web.app.service.GraphInfoService;
import com.asluax.lease.web.app.vo.graph.GraphVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【graph_info(图片信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class GraphInfoServiceImpl extends ServiceImpl<GraphInfoMapper, GraphInfo>
        implements GraphInfoService {

    @Override
    public List<GraphVo> getListByItemTypeAndId(ItemType itemType, Long id) {

        return baseMapper.getListByItemTypeAndId(itemType,id);
    }
}




