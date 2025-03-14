package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.common.utils.CopyUtil;
import com.asluax.lease.model.entity.GraphInfo;
import com.asluax.lease.model.entity.ViewAppointment;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.web.app.mapper.ViewAppointmentMapper;
import com.asluax.lease.web.app.service.ApartmentInfoService;
import com.asluax.lease.web.app.service.GraphInfoService;
import com.asluax.lease.web.app.service.ViewAppointmentService;
import com.asluax.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.asluax.lease.web.app.vo.apartment.ApartmentItemVo;
import com.asluax.lease.web.app.vo.appointment.AppointmentDetailVo;
import com.asluax.lease.web.app.vo.appointment.AppointmentItemVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class ViewAppointmentServiceImpl extends ServiceImpl<ViewAppointmentMapper, ViewAppointment>
        implements ViewAppointmentService {

    @Autowired
    ApartmentInfoService apartmentInfoService;
    @Autowired
    GraphInfoService graphInfoService;


    @Override
    public List<AppointmentItemVo> lisItem(Long userId) {
        return baseMapper.listItem(userId);
    }

    @Override
    public AppointmentDetailVo getDetailById(Long id) {
        ViewAppointment appointment = getById(id);
        AppointmentDetailVo appointmentDetailVo = CopyUtil.copyProperties(appointment, AppointmentDetailVo.class);
        ApartmentDetailVo apartmentDetailVo = apartmentInfoService.getDetailById(appointmentDetailVo.getApartmentId());
        ApartmentItemVo apartmentItemVo = CopyUtil.copyProperties(apartmentDetailVo, ApartmentItemVo.class);
        List<GraphInfo> graphInfos = graphInfoService.lambdaQuery()
                .eq(GraphInfo::getItemId, appointmentDetailVo.getApartmentId())
                .eq(GraphInfo::getItemType, ItemType.APARTMENT).list();
        apartmentItemVo.setGraphVoList(graphInfos);
        appointmentDetailVo.setApartmentItemVo(apartmentItemVo);
        return appointmentDetailVo;
    }
}




