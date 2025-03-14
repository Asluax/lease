package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.model.entity.FeeValue;
import com.asluax.lease.web.app.mapper.FeeValueMapper;
import com.asluax.lease.web.app.service.FeeValueService;
import com.asluax.lease.web.app.vo.fee.FeeValueVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【fee_value(杂项费用值表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class FeeValueServiceImpl extends ServiceImpl<FeeValueMapper, FeeValue>
        implements FeeValueService {

    @Override
    public List<FeeValueVo> getListByApartmentId(Long id) {
        return baseMapper.getListByApartmentId(id);
    }
}




