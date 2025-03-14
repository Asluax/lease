package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.model.entity.AttrValue;
import com.asluax.lease.web.app.mapper.AttrValueMapper;
import com.asluax.lease.web.app.service.AttrValueService;
import com.asluax.lease.web.app.vo.attr.AttrValueVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【attr_value(房间基本属性值表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class AttrValueServiceImpl extends ServiceImpl<AttrValueMapper, AttrValue>
        implements AttrValueService {

    @Override
    public List<AttrValueVo> getListByRoomId(Long id) {
        return baseMapper.getListByRoomId(id);
    }
}




