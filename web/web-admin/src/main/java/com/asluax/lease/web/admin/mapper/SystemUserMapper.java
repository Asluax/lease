package com.asluax.lease.web.admin.mapper;


import com.asluax.lease.model.entity.SystemUser;
import com.asluax.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.asluax.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * @author liubo
 * @description 针对表【system_user(员工信息表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.asluax.lease.model.SystemUser
 */
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    IPage<SystemUserItemVo> getSystemUserItemVoPage(@Param("iPage") IPage<SystemUserItemVo> iPage, @Param("queryVo") SystemUserQueryVo queryVo);
}




