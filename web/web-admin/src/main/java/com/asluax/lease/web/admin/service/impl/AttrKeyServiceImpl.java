package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.model.entity.AttrKey;
import com.asluax.lease.model.entity.AttrValue;
import com.asluax.lease.web.admin.mapper.AttrKeyMapper;
import com.asluax.lease.web.admin.service.AttrKeyService;
import com.asluax.lease.web.admin.vo.attr.AttrKeyVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author liubo
 * @description 针对表【attr_key(房间基本属性表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class AttrKeyServiceImpl extends ServiceImpl<AttrKeyMapper, AttrKey>
        implements AttrKeyService {

    @Override
    public ArrayList<AttrKeyVo> getAttrKeyVos(List<AttrKey> keys, List<AttrValue> values) {
        ArrayList<AttrKeyVo> vos = new ArrayList<>();
        for (AttrKey key : keys) {
            AttrKeyVo vo = new AttrKeyVo();
            List<AttrValue> list = values.stream()
                    .filter(attrValue -> Objects.equals(attrValue.getAttrKeyId(), key.getId()))
                    .toList();
            BeanUtils.copyProperties(key,vo);
            vo.setAttrValueList(list);
            vos.add(vo);
        }
        return vos;
    }
}




