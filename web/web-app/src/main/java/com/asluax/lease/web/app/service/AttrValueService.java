package com.asluax.lease.web.app.service;

import com.asluax.lease.model.entity.AttrValue;
import com.asluax.lease.web.app.vo.attr.AttrValueVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【attr_value(房间基本属性值表)】的数据库操作Service
 * @createDate 2023-07-26 11:12:39
 */
public interface AttrValueService extends IService<AttrValue> {

    List<AttrValueVo> getListByRoomId(Long id);
}
