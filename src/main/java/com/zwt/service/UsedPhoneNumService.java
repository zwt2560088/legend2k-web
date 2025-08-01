//package com.zwt.service;
//
//import com.smart.disguiser.agent.disguiser.agent.dao.sqlite.mapper.DeviceAccountTaskMapper;
//import com.smart.disguiser.agent.disguiser.agent.dao.sqlite.mapper.UsedPhoneNumMapper;
//import com.smart.disguiser.agent.disguiser.agent.dao.sqlite.model.DeviceAccountTask;
//import com.smart.disguiser.agent.disguiser.agent.dao.sqlite.model.UsedPhoneNum;
//import com.smart.disguiser.agent.disguiser.agent.enums.DeviceAccountTaskStatusEnum;
//import com.smart.disguiser.agent.disguiser.agent.model.task.web.PhoneNumQuery;
//import com.smart.disguiser.agent.disguiser.agent.util.DateUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * @Date: 2024/5/13 17:28
// */
//@Slf4j
//@Service
//public class UsedPhoneNumService {
//
//    @Resource
//    private UsedPhoneNumMapper usedPhoneNumMapper;
//
//    @Resource
//    private DeviceAccountTaskMapper deviceAccountTaskMapper;
//
//    /**
//     * 该方法故意不加事务
//     */
//    public void batchSave(List<String> phoneNumList, String competitorCode) {
//        if (CollectionUtils.isEmpty(phoneNumList)) {
//            return;
//        }
//
//        for (String s : phoneNumList) {
//            UsedPhoneNum usedPhoneNum = new UsedPhoneNum();
//            usedPhoneNum.setPhoneNum(s);
//            usedPhoneNum.setCtime(System.currentTimeMillis());
//            usedPhoneNum.setCompetitorCode(competitorCode);
//            try {
//                usedPhoneNumMapper.save(usedPhoneNum);
//            } catch (Exception e) {
//                log.warn("手机号保存失败, phoneNum:[{}]", s);
//            }
//        }
//    }
//
//    /**
//     * 只返回绑定了镜像的手机号
//     * @param phoneNumQuery
//     * @return
//     */
//    public List<String> listUsedPhoneNum(PhoneNumQuery phoneNumQuery) {
//        Integer startDt = phoneNumQuery.getStartDt();
//        Integer endDt = phoneNumQuery.getEndDt();
//        if (!ObjectUtils.allNotNull(startDt, endDt)) {
//            Integer dt = Integer.valueOf(DateUtils.getTodayDt());
//            startDt = dt;
//            endDt = dt;
//        }
//
//        List<DeviceAccountTask> taskList = deviceAccountTaskMapper.findByDtBetween(startDt, endDt);
//        return taskList.stream()
//                .filter(it -> Objects.equals(DeviceAccountTaskStatusEnum.RECEIVE_SUCCESS.getCode(), it.getRegisterStatus()))
//                .map(DeviceAccountTask::getPhoneNum)
//                .filter(StringUtils::isNotBlank)
//                .distinct()
//                .collect(Collectors.toList());
//    }
//
//}
