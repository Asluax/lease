package com.asluax.lease.web.admin.mapper;


import com.asluax.lease.model.entity.FeeKey;
import com.asluax.lease.web.admin.vo.fee.FeeKeyVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【fee_key(杂项费用名称表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.asluax.lease.model.FeeKey
 */
public interface FeeKeyMapper extends BaseMapper<FeeKey> {

    List<FeeKeyVo> getFeeList();
}




