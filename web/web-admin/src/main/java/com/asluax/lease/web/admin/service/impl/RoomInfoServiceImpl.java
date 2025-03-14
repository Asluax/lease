package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.model.entity.*;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.model.enums.LeaseStatus;
import com.asluax.lease.web.admin.mapper.RoomInfoMapper;
import com.asluax.lease.web.admin.service.*;
import com.asluax.lease.common.utils.CopyUtil;
import com.asluax.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.asluax.lease.web.admin.vo.attr.AttrValueVo;
import com.asluax.lease.web.admin.vo.graph.GraphVo;
import com.asluax.lease.web.admin.vo.room.RoomDetailVo;
import com.asluax.lease.web.admin.vo.room.RoomItemVo;
import com.asluax.lease.web.admin.vo.room.RoomQueryVo;
import com.asluax.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    ApartmentInfoService apartmentInfoService;
    @Autowired
    LeaseAgreementService leaseAgreementService;
    @Autowired
    RoomAttrValueService roomAttrValueService;
    @Autowired
    RoomFacilityService roomFacilityService;
    @Autowired
    RoomLabelService roomLabelService;
    @Autowired
    RoomPaymentTypeService roomPaymentTypeService;
    @Autowired
    RoomLeaseTermService roomLeaseTermService;
    @Autowired
    RoomInfoMapper roomInfoMapper;
    @Autowired
    AttrValueService attrValueService;
    @Autowired
    AttrKeyService attrKeyService;
    @Autowired
    FacilityInfoService facilityInfoService;
    @Autowired
    LabelInfoService labelInfoService;
    @Autowired
    PaymentTypeService paymentTypeService;
    @Autowired
    LeaseTermService leaseTermService;

    @Override
    public void saveOrUpdateByVo(RoomSubmitVo roomSubmitVo) {
        //* 保存或更新房间
        saveOrUpdate(roomSubmitVo);
        Long id = roomSubmitVo.getId();
        if (id != null) {
            removeOther(id);
        }
        //* 添加图片列表
        ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
        roomSubmitVo.getGraphVoList().forEach(graphVo -> {
                    GraphInfo graphInfo = CopyUtil.copyProperties(graphVo, GraphInfo.class);
                    graphInfo.setItemId(id);
                    graphInfo.setItemType(ItemType.ROOM);
                    graphInfoList.add(graphInfo);
                }
        );
        graphInfoService.saveBatch(graphInfoList);
        //* 添加属性信息列表
        ArrayList<RoomAttrValue> roomAttrValueList = new ArrayList<>();
        roomSubmitVo.getAttrValueIds().forEach(attrValue -> {
            RoomAttrValue roomAttrValue = new RoomAttrValue();
            roomAttrValue.setRoomId(id);
            roomAttrValue.setAttrValueId(attrValue);
            roomAttrValueList.add(roomAttrValue);
        });
        roomAttrValueService.saveBatch(roomAttrValueList);
        //* 添加标签信息列表
        ArrayList<RoomLabel> roomLabelList = new ArrayList<>();
        roomSubmitVo.getLabelInfoIds().forEach(labelInfo -> {
            RoomLabel roomLabel = new RoomLabel();
            roomLabel.setRoomId(id);
            roomLabel.setLabelId(labelInfo);
            roomLabelList.add(roomLabel);
        });
        roomLabelService.saveBatch(roomLabelList);
        // * 添加支付方式列表
        ArrayList<RoomPaymentType> roomPaymentTypeList = new ArrayList<>();
        roomSubmitVo.getPaymentTypeIds().forEach(paymentType -> {
            RoomPaymentType roomPaymentType = new RoomPaymentType();
            roomPaymentType.setRoomId(id);
            roomPaymentType.setPaymentTypeId(paymentType);
            roomPaymentTypeList.add(roomPaymentType);
        });
        roomPaymentTypeService.saveBatch(roomPaymentTypeList);
        //* 添加配套信息列表
        ArrayList<RoomFacility> roomFacilityList = new ArrayList<>();
        roomSubmitVo.getFacilityInfoIds().forEach( facilityInfo -> {
            RoomFacility roomFacility = new RoomFacility();
            roomFacility.setRoomId(id);
            roomFacility.setFacilityId(facilityInfo);
            roomFacilityList.add(roomFacility);
        });
        roomFacilityService.saveBatch(roomFacilityList);
        //* 添加可选租期列表
        ArrayList<RoomLeaseTerm> roomLeaseTermList = new ArrayList<>();
        roomSubmitVo.getLeaseTermIds().forEach(leaseTerm -> {
            RoomLeaseTerm roomLeaseTerm = new RoomLeaseTerm();
            roomLeaseTerm.setRoomId(id);
            roomLeaseTerm.setLeaseTermId(leaseTerm);
            roomLeaseTermList.add(roomLeaseTerm);
        });
        roomLeaseTermService.saveBatch(roomLeaseTermList);
    }

    private void removeOther(Long id) {
        //* 删除图片列表
        graphInfoService.remove(new LambdaQueryWrapper<GraphInfo>()
                .eq(GraphInfo::getItemType, ItemType.ROOM)
                .eq(GraphInfo::getItemId, id));
        //* 删除属性信息列表
        roomAttrValueService.remove(new LambdaQueryWrapper<RoomAttrValue>().eq(RoomAttrValue::getRoomId, id));
        //* 删除配套信息列表
        roomFacilityService.remove(new LambdaQueryWrapper<RoomFacility>().eq(RoomFacility::getRoomId, id));
        //* 删除标签信息列表
        roomLabelService.remove(new LambdaQueryWrapper<RoomLabel>().eq(RoomLabel::getRoomId, id));
        //* 删除支付方式列表
        roomPaymentTypeService.remove(new LambdaQueryWrapper<RoomPaymentType>().eq(RoomPaymentType::getRoomId, id));
        //*删除可选租期列表
        roomLeaseTermService.remove(new LambdaQueryWrapper<RoomLeaseTerm>().eq(RoomLeaseTerm::getRoomId, id));
    }

    @Override
    public List<RoomInfo> getListByQueryVo(RoomQueryVo queryVo) {
        if (queryVo.getApartmentId() != null) {
            return lambdaQuery().eq(RoomInfo::getApartmentId, queryVo.getApartmentId()).list();
        }
        ApartmentQueryVo vo = CopyUtil.copyProperties(queryVo, ApartmentQueryVo.class);
        List<ApartmentInfo> apartmentInfoList = apartmentInfoService.getApartmentByQueryVo(vo);
        List<Long> list = apartmentInfoList.stream().map(ApartmentInfo::getId).toList();
        return lambdaQuery().in(RoomInfo::getApartmentId, list).list();
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
                    if (leaseAgreements != null && !leaseAgreements.isEmpty()) {
                        LeaseAgreement leaseAgreement = leaseAgreements.get(0);
                        roomItemVo.setLeaseEndDate(leaseAgreement.getLeaseEndDate());
                        roomItemVo.setIsCheckIn(leaseAgreement.getStatus() == LeaseStatus.SIGNED
                                || leaseAgreement.getStatus() == LeaseStatus.WITHDRAWING
                                || leaseAgreement.getStatus() == LeaseStatus.RENEWING);
                    } else
                        roomItemVo.setIsCheckIn(false);
                    ApartmentInfo info = apartmentInfoService.getById(roomItemVo.getApartmentId());
                    roomItemVo.setApartmentInfo(info);
                }
        );
        return roomItemVos;
    }

    @Override
    public IPage<RoomItemVo> getRoomInfoByPageItem(IPage<RoomItemVo> iPage, RoomQueryVo queryVo) {
        return roomInfoMapper.getRoomInfoByPageItem(iPage,queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        RoomDetailVo roomDetailVo = CopyUtil.copyProperties(getById(id), RoomDetailVo.class);
        //* 添加公寓属性
        roomDetailVo.setApartmentInfo(apartmentInfoService.getById(roomDetailVo.getApartmentId()));
        //* 添加图片列表
        List<GraphInfo> graphInfos = graphInfoService.lambdaQuery()
                .eq(GraphInfo::getItemType, ItemType.ROOM)
                .eq(GraphInfo::getItemId, id).list();
        roomDetailVo.setGraphVoList(CopyUtil.copyList(graphInfos, GraphVo.class));
        //* 添加属性信息列表
        List<RoomAttrValue> roomAttrValueList = roomAttrValueService.lambdaQuery().eq(RoomAttrValue::getRoomId, id).list();
        if(!CollectionUtils.isEmpty(roomAttrValueList)){
            List<Long> alist = roomAttrValueList.stream().map(RoomAttrValue::getAttrValueId).toList();
            List<AttrValue> attrValueList = attrValueService.lambdaQuery().in(AttrValue::getId, alist).list();
            List<AttrValueVo> attrValueVoList = CopyUtil.copyList(attrValueList, AttrValueVo.class);
            attrValueVoList.forEach(attrValueVo -> {
                String name = attrKeyService.getById(attrValueVo.getAttrKeyId()).getName();
                attrValueVo.setAttrKeyName(name);
            });
            roomDetailVo.setAttrValueVoList(attrValueVoList);
        }
        //* 添加配套信息列表
        List<RoomFacility> roomFacilityList = roomFacilityService.lambdaQuery().eq(RoomFacility::getRoomId, id).list();
        if(!CollectionUtils.isEmpty(roomFacilityList)){
            List<Long> flist = roomFacilityList.stream().map(RoomFacility::getFacilityId).toList();
            List<FacilityInfo> facilityInfoList = facilityInfoService.lambdaQuery().in(FacilityInfo::getId, flist).list();
            roomDetailVo.setFacilityInfoList(facilityInfoList);
        }
        //* 添加标签信息列表
        List<RoomLabel> roomLabelList = roomLabelService.lambdaQuery().eq(RoomLabel::getRoomId, id).list();
        if(!CollectionUtils.isEmpty(roomLabelList)){
            List<Long> llist = roomLabelList.stream().map(RoomLabel::getLabelId).toList();
            List<LabelInfo> labelInfoList = labelInfoService.lambdaQuery().in(LabelInfo::getId, llist).list();
            roomDetailVo.setLabelInfoList(labelInfoList);
        }
        //* 添加支付方式列表
        List<RoomPaymentType> roomPaymentTypeList = roomPaymentTypeService.lambdaQuery().eq(RoomPaymentType::getRoomId, id).list();
        if(!CollectionUtils.isEmpty(roomPaymentTypeList)){
            List<Long> plist = roomPaymentTypeList.stream().map(RoomPaymentType::getPaymentTypeId).toList();
            List<PaymentType> paymentTypeList = paymentTypeService.lambdaQuery().in(PaymentType::getId, plist).list();
            roomDetailVo.setPaymentTypeList(paymentTypeList);
        }
        //* 添加可选租期列表
        List<RoomLeaseTerm> roomLeaseTermList = roomLeaseTermService.lambdaQuery().eq(RoomLeaseTerm::getRoomId, id).list();
        if(!CollectionUtils.isEmpty(roomLeaseTermList)){
            List<Long> ltlist = roomLeaseTermList.stream().map(RoomLeaseTerm::getLeaseTermId).toList();
            List<LeaseTerm> leaseTermList = leaseTermService.lambdaQuery().in(LeaseTerm::getId, ltlist).list();
            roomDetailVo.setLeaseTermList(leaseTermList);
        }
        return roomDetailVo;
    }

    @Override
    public void removeAllById(Long id) {
        removeOther(id);
        removeById(id);
    }

}




