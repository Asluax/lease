package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.model.entity.*;
import com.asluax.lease.model.enums.ItemType;
import com.asluax.lease.web.admin.mapper.ApartmentInfoMapper;
import com.asluax.lease.web.admin.service.*;
import com.asluax.lease.web.admin.utils.CopyUtil;
import com.asluax.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.asluax.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.asluax.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.asluax.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.asluax.lease.web.admin.vo.fee.FeeValueVo;
import com.asluax.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    LeaseAgreementService leaseAgreementService;
    @Lazy
    @Autowired
    RoomInfoService roomInfoService;

    @Autowired
    ApartmentFacilityService apartmentFacilityService;
    @Autowired
    ApartmentLabelService apartmentLabelService;
    @Autowired
    ApartmentFeeValueService apartmentFeeValueService;
    @Autowired
    GraphInfoService graphInfoService;
    @Autowired
    LabelInfoService labelInfoService;
    @Autowired
    FacilityInfoService facilityInfoService;
    @Autowired
    FeeKeyService feeKeyService;
    @Autowired
    FeeValueService feeValueService;

    @Override
    public void saveOrUpdateVo(ApartmentSubmitVo apartmentSubmitVo) {
        List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        ApartmentInfo apartmentInfo = CopyUtil.copyProperties(apartmentSubmitVo, ApartmentInfo.class);
        //*写入设施列表
        List<ApartmentFacility> apartmentFacilityList = CopyUtil.copyList(facilityInfoIds, ApartmentFacility.class);
        apartmentFacilityList.forEach(apartmentFacility -> apartmentFacility.setApartmentId(apartmentInfo.getId()));
        apartmentFacilityService.saveOrUpdateBatch(apartmentFacilityList);
        //*写入图片列表
        List<GraphInfo> graphInfos = CopyUtil.copyList(graphVoList, GraphInfo.class);
        graphInfos.forEach(graphInfo -> {
            graphInfo.setItemType(ItemType.APARTMENT);
            graphInfo.setItemId(apartmentInfo.getId());
        });
        graphInfoService.saveOrUpdateBatch(graphInfos);
        //*写入杂费列表
        List<ApartmentFeeValue> apartmentFeeValues = CopyUtil.copyList(feeValueIds, ApartmentFeeValue.class);
        apartmentFeeValues.forEach(apartmentFeeValue -> apartmentFeeValue.setApartmentId(apartmentInfo.getId()));
        apartmentFeeValueService.saveOrUpdateBatch(apartmentFeeValues);
        //*写入标签列表
        List<ApartmentLabel> apartmentLabels = CopyUtil.copyList(labelIds, ApartmentLabel.class);
        apartmentLabels.forEach(apartmentLabel -> apartmentLabel.setApartmentId(apartmentInfo.getId()));
        apartmentLabelService.saveOrUpdateBatch(apartmentLabels);
    }

    //* 根据公寓ID查询数据，返回VO对象(图片VO列表，公寓标签列表，公寓配套列表,工艺杂费VO列表)
    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        ApartmentDetailVo vo = new ApartmentDetailVo();
        ApartmentInfo apartment = getById(id);
        BeanUtils.copyProperties(apartment, vo);
        Long apartmentId = apartment.getId();
        //* 图片VO封装
        List<GraphInfo> graphInfoList = graphInfoService.lambdaQuery()
                .eq(GraphInfo::getItemType, 1)
                .eq(GraphInfo::getItemId, apartmentId).list();
        List<GraphVo> graphVos = CopyUtil.copyList(graphInfoList, GraphVo.class);
        vo.setGraphVoList(graphVos);
        //* 公寓标签列表封装
        List<ApartmentLabel> apartmentLabelList = apartmentLabelService.lambdaQuery().eq(ApartmentLabel::getApartmentId, apartmentId).list();
        List<Long> lableList = apartmentLabelList.stream().map(ApartmentLabel::getLabelId).toList();
        List<LabelInfo> labelInfoList = labelInfoService.lambdaQuery().in(LabelInfo::getId, lableList).list();
        vo.setLabelInfoList(labelInfoList);
        //* 公寓配套列表封装
        List<ApartmentFacility> apartmentFacilityList = apartmentFacilityService.lambdaQuery().eq(ApartmentFacility::getApartmentId, apartmentId).list();
        List<Long> facilityList = apartmentFacilityList.stream().map(ApartmentFacility::getFacilityId).toList();
        List<FacilityInfo> facilityInfoList = facilityInfoService.lambdaQuery().in(FacilityInfo::getId, facilityList).list();
        vo.setFacilityInfoList(facilityInfoList);
        //* 杂费列表封装
        List<ApartmentFeeValue> feeList = apartmentFeeValueService.lambdaQuery().eq(ApartmentFeeValue::getApartmentId, apartmentId).list();
        List<Long> feeValueList = feeList.stream().map(ApartmentFeeValue::getFeeValueId).toList();
        List<FeeValue> feeValues = feeValueService.lambdaQuery().in(FeeValue::getId, feeValueList).list();
        List<FeeValueVo> feeValueVoList = CopyUtil.copyList(feeValues, FeeValueVo.class);
        feeValueVoList.forEach(feeValueVo -> feeValueVo.setFeeKeyName(feeKeyService.getById(feeValueVo.getFeeKeyId()).getName()));
        vo.setFeeValueVoList(feeValueVoList);
        return vo;
    }

    //* 条件查询公寓信息
    @Override
    public List<ApartmentInfo> getApartmentByQueryVo(ApartmentQueryVo queryVo) {
        HashMap<SFunction<ApartmentInfo, ?>, Long> apartmentMap = new HashMap<>();
        if (queryVo.getProvinceId() != null)
            apartmentMap.put(ApartmentInfo::getProvinceId, queryVo.getProvinceId());
        if (queryVo.getCityId() != null)
            apartmentMap.put(ApartmentInfo::getCityId, queryVo.getCityId());
        if (queryVo.getDistrictId() != null)
            apartmentMap.put(ApartmentInfo::getDistrictId, queryVo.getDistrictId());
        return this.lambdaQuery().allEq(apartmentMap).list();
    }

    //* 公寓信息封装为VO
    @Override
    public List<ApartmentItemVo> getVoList(List<ApartmentInfo> apartmentInfoList) {
        List<LeaseAgreement> leaseAgreements = leaseAgreementService.lambdaQuery()
                .eq(LeaseAgreement::getStatus, 2).or()
                .eq(LeaseAgreement::getStatus, 5).or()
                .eq(LeaseAgreement::getStatus, 7).list();
        HashMap<Long, Integer> numMap = new HashMap<>();
        apartmentInfoList.forEach(apartmentInfo -> numMap.put(apartmentInfo.getId(), 0));
        leaseAgreements.forEach(leaseAgreement -> {
            Integer i = numMap.get(leaseAgreement.getApartmentId());
            numMap.put(leaseAgreement.getApartmentId(), ++i);
        });
        List<ApartmentItemVo> list = new ArrayList<>();
        apartmentInfoList.forEach(apartmentInfo -> {
            //*根据公寓ID找到所属房间ID
            LambdaQueryChainWrapper<RoomInfo> queryWrapper = roomInfoService.lambdaQuery().eq(RoomInfo::getApartmentId, apartmentInfo.getId());
            Long totalRoomCount = queryWrapper.count();//*房间总数//*剩余房间数
            Long freeRoomCount = totalRoomCount - numMap.get(apartmentInfo.getId());
            ApartmentItemVo vo = new ApartmentItemVo();
            BeanUtils.copyProperties(apartmentInfo, vo);
            vo.setTotalRoomCount(totalRoomCount);
            vo.setFreeRoomCount(freeRoomCount);
            list.add(vo);
        });
        return list;
    }
}




