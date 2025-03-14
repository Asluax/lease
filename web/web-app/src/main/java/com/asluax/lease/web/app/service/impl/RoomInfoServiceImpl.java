package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.common.constant.LoginUserContext;
import com.asluax.lease.common.utils.CopyUtil;
import com.asluax.lease.model.entity.GraphInfo;
import com.asluax.lease.model.entity.LeaseAgreement;
import com.asluax.lease.model.entity.RoomInfo;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.model.enums.LeaseStatus;
import com.asluax.lease.web.app.mapper.LeaseAgreementMapper;
import com.asluax.lease.web.app.mapper.RoomInfoMapper;
import com.asluax.lease.web.app.service.*;
import com.asluax.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.asluax.lease.web.app.vo.apartment.ApartmentItemVo;
import com.asluax.lease.web.app.vo.room.RoomDetailVo;
import com.asluax.lease.web.app.vo.room.RoomItemVo;
import com.asluax.lease.web.app.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
@Slf4j
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {
    @Autowired
    ApartmentInfoService apartmentInfoService;
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
    FeeValueService feeValueService;
    @Autowired
    LeaseTermService leaseTermService;
    @Autowired
    LeaseAgreementMapper leaseAgreementMapper;
    @Autowired
    BrowsingHistoryService browsingHistoryService;


    @Override
    public IPage<RoomItemVo> pageItem(IPage<RoomItemVo> page, RoomQueryVo queryVo) {
        return baseMapper.pageItem(page,queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        RoomInfo roomInfo = baseMapper.getDetailById(id);
        if(roomInfo == null)
            return null;
        RoomDetailVo roomDetailVo = CopyUtil.copyProperties(roomInfo, RoomDetailVo.class);
        //* 设置公寓信息
        ApartmentDetailVo apartmentDetailVo = apartmentInfoService.getDetailById(roomDetailVo.getApartmentId());
        ApartmentItemVo apartmentItemVo = CopyUtil.copyProperties(apartmentDetailVo, ApartmentItemVo.class);
        List<GraphInfo> graphInfos = graphInfoService.lambdaQuery()
                .eq(GraphInfo::getItemId, roomDetailVo.getApartmentId())
                .eq(GraphInfo::getItemType, ItemType.APARTMENT).list();
        apartmentItemVo.setGraphVoList(graphInfos);
        roomDetailVo.setApartmentItemVo(apartmentItemVo);
        //* 设置图片列表
        roomDetailVo.setGraphVoList(graphInfoService.getListByItemTypeAndId(ItemType.ROOM, id));
        //* 设置属性信息列表
        roomDetailVo.setAttrValueVoList(attrValueService.getListByRoomId(id));
        //* 设置配套信息列表
        roomDetailVo.setFacilityInfoList(facilityInfoService.getListByRoomId(id));
        //* 设置标签信息列表
        roomDetailVo.setLabelInfoList(labelInfoService.getListByRoomId(id));
        //* 设置支付方式列表
        roomDetailVo.setPaymentTypeList(paymentTypeService.getListByRoomId(id));
        //* 设置杂费列表
        roomDetailVo.setFeeValueVoList(feeValueService.getListByApartmentId(roomDetailVo.getApartmentId()));
        //* 设置租期列表
        roomDetailVo.setLeaseTermList(leaseTermService.getListByRoomId(id));
        //* 设置是否删除
        roomDetailVo.setIsDelete(roomDetailVo.getIsDeleted()==1);
        //* 设置是否入住
        LambdaQueryWrapper<LeaseAgreement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LeaseAgreement::getRoomId, roomInfo.getId());
        queryWrapper.in(LeaseAgreement::getStatus, LeaseStatus.SIGNED, LeaseStatus.WITHDRAWING);
        Long singedCount = leaseAgreementMapper.selectCount(queryWrapper);

        roomDetailVo.setIsCheckIn(singedCount > 0);
        browsingHistoryService.saveHistory(LoginUserContext.getLoginUserApp().getUserId(), id);
        return roomDetailVo;
    }

    @Override
    public IPage<RoomItemVo> pageItemByApartmentId(IPage<RoomItemVo> page, Long id) {
        return baseMapper.pageItemByApartmentId(page,id);
    }
}




