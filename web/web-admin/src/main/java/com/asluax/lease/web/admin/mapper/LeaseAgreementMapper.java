package com.asluax.lease.web.admin.mapper;


import com.asluax.lease.model.entity.LeaseAgreement;
import com.asluax.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.asluax.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;


/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.asluax.lease.model.LeaseAgreement
 */
public interface LeaseAgreementMapper extends BaseMapper<LeaseAgreement> {

    IPage<AgreementVo> getByPage(@Param("page") IPage<AgreementVo> page, @Param("queryVo") AgreementQueryVo queryVo);

}




