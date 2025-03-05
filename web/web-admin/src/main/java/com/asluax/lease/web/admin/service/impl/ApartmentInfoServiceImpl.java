package com.asluax.lease.web.admin.service.impl;

import com.asluax.lease.model.entity.ApartmentInfo;
import com.asluax.lease.model.entity.LeaseAgreement;
import com.asluax.lease.model.entity.RoomInfo;
import com.asluax.lease.web.admin.mapper.ApartmentInfoMapper;
import com.asluax.lease.web.admin.service.ApartmentInfoService;
import com.asluax.lease.web.admin.service.LeaseAgreementService;
import com.asluax.lease.web.admin.service.RoomInfoService;
import com.asluax.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.asluax.lease.web.admin.vo.apartment.ApartmentQueryVo;
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




