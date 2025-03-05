package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.model.entity.*;
import com.asluax.lease.web.admin.mapper.RoomInfoMapper;
import com.asluax.lease.web.admin.service.*;
import com.asluax.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.asluax.lease.web.admin.vo.room.RoomItemVo;
import com.asluax.lease.web.admin.vo.room.RoomQueryVo;
import com.asluax.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public void saveOrUpdateByVo(RoomSubmitVo roomSubmitVo) {
        List<GraphInfo> graphInfos = copyList(roomSubmitVo.getGraphVoList(), GraphInfo.class);
        List<AttrValue> attrValues = copyList(roomSubmitVo.getAttrValueIds(), AttrValue.class);
        List<FacilityInfo> facilityInfos = copyList(roomSubmitVo.getFacilityInfoIds(), FacilityInfo.class);
        List<LabelInfo> labelInfos = copyList(roomSubmitVo.getLabelInfoIds(), LabelInfo.class);
        List<PaymentType> paymentTypes = copyList(roomSubmitVo.getPaymentTypeIds(), PaymentType.class);
        List<LeaseTerm> leaseTerms = copyList(roomSubmitVo.getLeaseTermIds(), LeaseTerm.class);
        RoomInfo roomInfo = copyProperties(roomSubmitVo, RoomInfo.class);
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
        ApartmentQueryVo vo = copyProperties(queryVo, ApartmentQueryVo.class);
        List<ApartmentInfo> apartmentInfoList = apartmentInfoService.getApartmentByQueryVo(vo);
        List<ApartmentInfo> list = apartmentInfoList.stream().filter(apartmentInfo ->
                        Objects.equals(apartmentInfo.getId(), queryVo.getApartmentId()))
                .toList();
        HashMap<SFunction<RoomInfo, ?>, Long> map = new HashMap<>();
        list.forEach(apartmentInfo ->
                map.put(RoomInfo::getApartmentId,apartmentInfo.getId()));

        return lambdaQuery().allEq(map).list();
    }

    @Override
    public List<RoomItemVo> getVoList(List<RoomInfo> roomInfoList) {
        return List.of();
    }

    //*对象拷贝
    public static <S, T> T copyProperties(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("对象拷贝失败", e);
        }
    }
    //*列表对象拷贝
    public static <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream()
                .map(source -> copyProperties(source, targetClass))
                .collect(Collectors.toList());
    }
}




