package com.asluax.lease.web.app.service.impl;

import com.asluax.lease.common.utils.CopyUtil;
import com.asluax.lease.model.entity.*;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.web.app.mapper.ApartmentInfoMapper;
import com.asluax.lease.web.app.service.*;
import com.asluax.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.asluax.lease.web.app.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {
    @Lazy
    @Autowired
    RoomInfoService roomInfoService;
    @Autowired
    GraphInfoService graphInfoService;
    @Autowired
    LabelInfoService labelInfoService;
    @Autowired
    FacilityInfoService facilityInfoService;
    @Autowired
    ApartmentLabelService apartmentLabelService;
    @Autowired
    ApartmentFacilityService apartmentFacilityService;

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        ApartmentInfo apartmentInfo = getById(id);
        ApartmentDetailVo apartmentDetailVo = CopyUtil.copyProperties(apartmentInfo, ApartmentDetailVo.class);
        List<GraphInfo> graphInfos = graphInfoService.lambdaQuery().
                eq(GraphInfo::getItemType, ItemType.APARTMENT).
                eq(GraphInfo::getItemId, id).list();
        List<GraphVo> graphVos = CopyUtil.copyList(graphInfos, GraphVo.class);
        apartmentDetailVo.setGraphVoList(graphVos);
        List<ApartmentLabel> labelList = apartmentLabelService.lambdaQuery()
                .eq(ApartmentLabel::getApartmentId, id).list();
        List<Long> list = labelList.stream().map(ApartmentLabel::getLabelId).toList();
        if(CollectionUtils.isNotEmpty(labelList)){
            List<LabelInfo> labelInfos = labelInfoService.lambdaQuery().in(LabelInfo::getId, list).list();
            apartmentDetailVo.setLabelInfoList(labelInfos);
        }
        List<ApartmentFacility> facilityList = apartmentFacilityService.lambdaQuery()
                .eq(ApartmentFacility::getApartmentId, id).list();
        List<Long> flist = facilityList.stream().map(ApartmentFacility::getFacilityId).toList();
        if(CollectionUtils.isNotEmpty(facilityList)){
            List<FacilityInfo> facilityInfos = facilityInfoService.lambdaQuery().in(FacilityInfo::getId, flist).list();
            apartmentDetailVo.setFacilityInfoList(facilityInfos);
        }
        ArrayList<BigDecimal> rentList = new ArrayList<>();
        roomInfoService.list().forEach(roomInfo -> rentList.add(roomInfo.getRent()));
        BigDecimal minRent = rentList.stream().min(BigDecimal::compareTo).orElse(new BigDecimal(0));
        apartmentDetailVo.setMinRent(minRent);
        apartmentDetailVo.setIsDelete(apartmentInfo.getIsDeleted() == 0);
        return apartmentDetailVo;
    }
}




