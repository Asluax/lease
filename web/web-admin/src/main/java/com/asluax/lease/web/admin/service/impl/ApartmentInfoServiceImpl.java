package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.common.exception.MyAssert;
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
    @Autowired
    ProvinceInfoService provinceInfoService;
    @Autowired
    CityInfoService cityInfoService;
    @Autowired
    DistrictInfoService districtInfoService;
    @Lazy
    @Autowired
    ApartmentInfoService apartmentInfoService;

    @Override
    public void saveOrUpdateVo(ApartmentSubmitVo apartmentSubmitVo) {
        List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        ProvinceInfo provinceInfo = provinceInfoService.getById(apartmentSubmitVo.getProvinceId());
        CityInfo cityInfo = cityInfoService.getById(apartmentSubmitVo.getCityId());
        DistrictInfo districtInfo = districtInfoService.getById(apartmentSubmitVo.getDistrictId());
        apartmentSubmitVo.setProvinceName(provinceInfo.getName());
        apartmentSubmitVo.setCityName(cityInfo.getName());
        apartmentSubmitVo.setDistrictName(districtInfo.getName());
        saveOrUpdate(apartmentSubmitVo);
        Long id = apartmentSubmitVo.getId();
        if (id != null) {
            //* id为空执行更新操作，先删除公寓关系表
            removeApartmentOther(id);
        }
        // *新增配套关系
        ArrayList<ApartmentFacility> apartmentFacilityList = new ArrayList<>();
        facilityInfoIds.forEach(facilityInfoId -> {
            ApartmentFacility apartmentFacility = new ApartmentFacility();
            apartmentFacility.setApartmentId(id);
            apartmentFacility.setFacilityId(facilityInfoId);
            apartmentFacilityList.add(apartmentFacility);
        });
        apartmentFacilityService.saveBatch(apartmentFacilityList);
        //* 新增图片
        List<GraphInfo> graphInfoList = CopyUtil.copyList(graphVoList, GraphInfo.class);
        graphInfoList.forEach(graphInfo -> {
            graphInfo.setItemType(ItemType.APARTMENT);
            graphInfo.setItemId(id);
        });
        graphInfoService.saveBatch(graphInfoList);
        //* 新增杂费关系
        ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
        feeValueIds.forEach(feeValueId -> {
            ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();
            apartmentFeeValue.setFeeValueId(feeValueId);
            apartmentFeeValue.setApartmentId(id);
            apartmentFeeValueList.add(apartmentFeeValue);
        });
        apartmentFeeValueService.saveBatch(apartmentFeeValueList);
        //* 新增标签关系
        ArrayList<ApartmentLabel> apartmentLabelList = new ArrayList<>();
        labelIds.forEach(labelId -> {
            ApartmentLabel apartmentLabel = new ApartmentLabel();
            apartmentLabel.setApartmentId(id);
            apartmentLabel.setLabelId(labelId);
            apartmentLabelList.add(apartmentLabel);
        });
        apartmentLabelService.saveBatch(apartmentLabelList);
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
        if (!CollectionUtils.isEmpty(lableList)) {
            List<LabelInfo> labelInfoList = labelInfoService.lambdaQuery().in(LabelInfo::getId, lableList).list();
            vo.setLabelInfoList(labelInfoList);
        }
        //* 公寓配套列表封装
        List<ApartmentFacility> apartmentFacilityList = apartmentFacilityService.lambdaQuery().eq(ApartmentFacility::getApartmentId, apartmentId).list();
        List<Long> facilityList = apartmentFacilityList.stream().map(ApartmentFacility::getFacilityId).toList();
        if (!CollectionUtils.isEmpty(facilityList)) {
            List<FacilityInfo> facilityInfoList = facilityInfoService.lambdaQuery().in(FacilityInfo::getId, facilityList).list();
            vo.setFacilityInfoList(facilityInfoList);
        }
        //* 杂费列表封装
        List<ApartmentFeeValue> feeList = apartmentFeeValueService.lambdaQuery().eq(ApartmentFeeValue::getApartmentId, apartmentId).list();
        List<Long> feeValueList = feeList.stream().map(ApartmentFeeValue::getFeeValueId).toList();
        if (!CollectionUtils.isEmpty(feeValueList)) {
            List<FeeValue> feeValues = feeValueService.lambdaQuery().in(FeeValue::getId, feeValueList).list();
            List<FeeValueVo> feeValueVoList = CopyUtil.copyList(feeValues, FeeValueVo.class);
            feeValueVoList.forEach(feeValueVo -> feeValueVo.setFeeKeyName(feeKeyService.getById(feeValueVo.getFeeKeyId()).getName()));
            vo.setFeeValueVoList(feeValueVoList);
        }
        return vo;
    }

    //*删除公寓及相关内容
    @Override
    public void removeApartmentById(Long id) {
        //* 删除公寓标签关联
        //* 删除公寓杂费关联
        //* 删除公寓设施关联
        //* 删除公寓图片
        Long count = roomInfoService.lambdaQuery().eq(RoomInfo::getApartmentId, id).count();
        MyAssert.isTrue(count <= 0, "公寓存在房间，无法删除");
        removeApartmentOther(id);
        apartmentInfoService.removeById(id);
    }

    private void removeApartmentOther(Long id) {
        apartmentFacilityService.remove(new LambdaQueryWrapper<ApartmentFacility>().eq(ApartmentFacility::getApartmentId, id));
        graphInfoService.remove(new LambdaQueryWrapper<GraphInfo>().eq(GraphInfo::getItemType, ItemType.APARTMENT).eq(GraphInfo::getItemId, id));
        apartmentFeeValueService.remove(new LambdaQueryWrapper<ApartmentFeeValue>().eq(ApartmentFeeValue::getApartmentId, id));
        apartmentLabelService.remove(new LambdaQueryWrapper<ApartmentLabel>().eq(ApartmentLabel::getApartmentId, id));
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




