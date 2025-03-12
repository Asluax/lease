package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.model.entity.*;
import com.asluax.lease.web.app.mapper.*;
import com.asluax.lease.web.app.service.LeaseAgreementService;
import com.asluax.lease.web.app.vo.agreement.AgreementDetailVo;
import com.asluax.lease.web.app.vo.agreement.AgreementItemVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {


    @Override
    public List<AgreementItemVo> listItem(String phone) {
        return baseMapper.listItem(phone);
    }

    @Override
    public AgreementDetailVo getDetailById(Long id) {
        return baseMapper.getDetailById(id);
    }
}




