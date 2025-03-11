package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.model.entity.SystemUser;
import com.asluax.lease.web.admin.mapper.SystemUserMapper;
import com.asluax.lease.web.admin.service.SystemUserService;
import com.asluax.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.asluax.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【system_user(员工信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser>
        implements SystemUserService {

    @Override
    public IPage<SystemUserItemVo> getSystemUserItemVoPage(IPage<SystemUserItemVo> iPage, SystemUserQueryVo queryVo) {
        return baseMapper.getSystemUserItemVoPage(iPage,queryVo);
    }

    @Override
    public SystemUserItemVo getByIdForVo(Long id) {
        IPage<SystemUserItemVo> page = getSystemUserItemVoPage(new Page<>(-1, -1), null);
        List<SystemUserItemVo> list = page.getRecords();
        if(!CollectionUtils.isEmpty(list)){
            return list.get(1);
        }
        return null;
    }
}




