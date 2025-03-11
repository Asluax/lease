package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.model.entity.LeaseAgreement;
import com.asluax.lease.web.admin.mapper.LeaseAgreementMapper;
import com.asluax.lease.web.admin.service.LeaseAgreementService;
import com.asluax.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.asluax.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    @Override
    public IPage<AgreementVo> getByPage(IPage<AgreementVo> page, AgreementQueryVo queryVo) {
        return baseMapper.getByPage(page,queryVo);
    }

    @Override
    public AgreementVo getByIdVo(Long id) {
        IPage<AgreementVo> page = baseMapper.getByPage(new Page<>(-1,-1),new AgreementQueryVo());
        List<AgreementVo> list = page.getRecords().stream().filter(agreementVo ->
                Objects.equals(agreementVo.getId(), id)).toList();
        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
}




