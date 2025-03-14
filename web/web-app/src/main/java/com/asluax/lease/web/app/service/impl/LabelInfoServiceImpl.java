package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.model.entity.LabelInfo;
import com.asluax.lease.web.app.mapper.LabelInfoMapper;
import com.asluax.lease.web.app.service.LabelInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【label_info(标签信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LabelInfoServiceImpl extends ServiceImpl<LabelInfoMapper, LabelInfo>
        implements LabelInfoService {

    @Override
    public List<LabelInfo> getListByRoomId(Long id) {
        return baseMapper.getListByRoomId(id);
    }
}




