package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.model.entity.*;
import com.asluax.lease.model.enums.LeaseStatus;
import com.asluax.lease.web.admin.mapper.RoomInfoMapper;
import com.asluax.lease.web.admin.service.*;
import com.asluax.lease.web.admin.utils.CopyUtil;
import com.asluax.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.asluax.lease.web.admin.vo.room.RoomItemVo;
import com.asluax.lease.web.admin.vo.room.RoomQueryVo;
import com.asluax.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {
    @Autowired
    GraphInfoService graphInfoService;
    @Autowired
    AttrValueService attrValueService;
    @Autowired
    FacilityInfoService facilityInfoService;
    @Autowired
    LabelInfoService labelInfoService;
    @Autowired
    PaymentTypeService paymentTypeService;
    @Autowired
    LeaseTermService leaseTermService;
    @Autowired
    ApartmentInfoService apartmentInfoService;
    @Autowired
    LeaseAgreementService leaseAgreementService;

    //todo 有问题
    @Override
    public void saveOrUpdateByVo(RoomSubmitVo roomSubmitVo) {
        List<GraphInfo> graphInfos = CopyUtil.copyList(roomSubmitVo.getGraphVoList(), GraphInfo.class);
        List<AttrValue> attrValues = CopyUtil.copyList(roomSubmitVo.getAttrValueIds(), AttrValue.class);
        List<FacilityInfo> facilityInfos = CopyUtil.copyList(roomSubmitVo.getFacilityInfoIds(), FacilityInfo.class);
        List<LabelInfo> labelInfos = CopyUtil.copyList(roomSubmitVo.getLabelInfoIds(), LabelInfo.class);
        List<PaymentType> paymentTypes = CopyUtil.copyList(roomSubmitVo.getPaymentTypeIds(), PaymentType.class);
        List<LeaseTerm> leaseTerms = CopyUtil.copyList(roomSubmitVo.getLeaseTermIds(), LeaseTerm.class);
        RoomInfo roomInfo =CopyUtil.copyProperties(roomSubmitVo, RoomInfo.class);
        graphInfoService.saveOrUpdateBatch(graphInfos);
        attrValueService.saveOrUpdateBatch(attrValues);
        facilityInfoService.saveOrUpdateBatch(facilityInfos);
        labelInfoService.saveOrUpdateBatch(labelInfos);
        paymentTypeService.saveOrUpdateBatch(paymentTypes);
        leaseTermService.saveOrUpdateBatch(leaseTerms);
        saveOrUpdate(roomInfo);
    }

    @Override
    public List<RoomInfo> getListByQueryVo(RoomQueryVo queryVo) {
        if (queryVo.getApartmentId()!=null){
            return lambdaQuery().eq(RoomInfo::getApartmentId,queryVo.getApartmentId()).list();
        }
        ApartmentQueryVo vo = CopyUtil.copyProperties(queryVo, ApartmentQueryVo.class);
        List<ApartmentInfo> apartmentInfoList = apartmentInfoService.getApartmentByQueryVo(vo);
        List<Long> list = apartmentInfoList.stream().map(ApartmentInfo::getId).toList();
        return lambdaQuery().in(RoomInfo::getApartmentId,list).list();
    }

    @Override
    public List<RoomItemVo> getVoList(List<RoomInfo> roomInfoList) {
        List<RoomItemVo> roomItemVos = CopyUtil.copyList(roomInfoList, RoomItemVo.class);
        roomItemVos.forEach(
                roomItemVo -> {
                    List<LeaseAgreement> leaseAgreements = leaseAgreementService.lambdaQuery()
                            .eq(LeaseAgreement::getRoomId, roomItemVo.getId())
                            .ne(LeaseAgreement::getStatus, LeaseStatus.EXPIRED)
                            .orderByAsc(LeaseAgreement::getLeaseEndDate).list();
                    if (leaseAgreements!=null && !leaseAgreements.isEmpty()) {
                        LeaseAgreement leaseAgreement = leaseAgreements.get(0);
                        roomItemVo.setLeaseEndDate(leaseAgreement.getLeaseEndDate());
                        roomItemVo.setIsCheckIn(leaseAgreement.getStatus() == LeaseStatus.SIGNED
                                || leaseAgreement.getStatus() == LeaseStatus.WITHDRAWING
                                || leaseAgreement.getStatus() == LeaseStatus.RENEWING);
                    }
                    roomItemVo.setIsCheckIn(false);
                    ApartmentInfo info = apartmentInfoService.getById(roomItemVo.getApartmentId());
                    roomItemVo.setApartmentInfo(info);
                }
        );
        return roomItemVos;
    }

}




