package com.asluax.lease.web.app.service;

import com.asluax.lease.model.entity.FeeValue;
import com.asluax.lease.web.app.vo.fee.FeeValueVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【fee_value(杂项费用值表)】的数据库操作Service
 * @createDate 2023-07-26 11:12:39
 */
public interface FeeValueService extends IService<FeeValue> {

    List<FeeValueVo> getListByApartmentId(Long id);
}
