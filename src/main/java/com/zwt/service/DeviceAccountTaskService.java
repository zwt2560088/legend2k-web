package com.zwt.service;

import com.alibaba.fastjson.JSON;
import com.zwt.enums.DeviceAccountTaskStatusEnum;
import com.zwt.exception.BizException;
import com.zwt.exception.WarningException;
import com.zwt.model.account.AddRecordDTO;
import com.zwt.model.task.DeviceAccountTaskListVO;
import com.zwt.model.task.web.RegisterTaskDTO;
import com.zwt.model.task.web.TaskDTO;
import com.zwt.model.task.web.TaskQuery;
import com.zwt.model.task.web.UpdateTaskStatusRequest;
import com.zwt.repo.sqlite.mapper.AccountDetailMapper;
import com.zwt.repo.sqlite.mapper.DeviceAccountTaskMapper;
import com.zwt.repo.sqlite.mapper.UsedPhoneNumMapper;
import com.zwt.repo.sqlite.model.DeviceAccountTask;
import com.zwt.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.zwt.enums.DeviceAccountTaskStatusEnum.TO_RECEIVE_CODE;



/**
 * @Author:
 * @Date: 2024/5/10 16:37
 */
@Service
@Slf4j
public class DeviceAccountTaskService {

    @Resource
    private DeviceAccountTaskMapper deviceAccountTaskMapper;

    @Resource
    private UsedPhoneNumMapper usedPhoneNumMapper;

//    @Resource
//    private ConfigService configService;

    @Resource
    private AccountRecordService accountRecordService;
    @Autowired
    private AccountDetailMapper accountDetailMapper;


    public List<DeviceAccountTaskListVO> queryTask(TaskQuery taskQuery) {
        String todayDt = DateUtils.getTodayDt();
        Integer startDt = Integer.valueOf(todayDt);
        Integer endDt = Integer.valueOf(todayDt);

        if (Objects.nonNull(taskQuery.getStartDt())) {
            startDt = Integer.valueOf(DateUtils.getDt(taskQuery.getStartDt()));
        }
        if (Objects.nonNull(taskQuery.getEndDt())) {
            endDt = Integer.valueOf(DateUtils.getDt(taskQuery.getEndDt()));
        }
        List<DeviceAccountTask> list;
//        Sort sort = configService.digTaskListSort();
        boolean queryToDealTask = !taskQuery.getQueryAll() && Objects.isNull(taskQuery.getQueryStatus());
        if (taskQuery.getQueryAll() || queryToDealTask) {
            list = deviceAccountTaskMapper.findByDtBetween(startDt, endDt);
            if (queryToDealTask) {
                list = list.stream()
                        .filter(it -> DeviceAccountTaskStatusEnum.canUpdate(it.getRegisterStatus()))
                        .collect(Collectors.toList());
            }
        } else {
            list = deviceAccountTaskMapper.findByDtBetweenAndRegisterStatus(startDt, endDt, taskQuery.getQueryStatus());
        }
        if (Objects.nonNull(taskQuery.getAccountSource())) {
            list = list.stream()
                    .filter(it -> taskQuery.getAccountSource().equals(it.getAccountSource()))
                    .collect(Collectors.toList());
        }
        return ListUtils.emptyIfNull(list).stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private DeviceAccountTaskListVO convert(DeviceAccountTask task) {
        DeviceAccountTaskListVO listVO = new DeviceAccountTaskListVO();
        BeanUtils.copyProperties(task, listVO);
        listVO.setStatusValue(DeviceAccountTaskStatusEnum.getDescByCode(task.getRegisterStatus()));
        listVO.setNeedDeal(DeviceAccountTaskStatusEnum.canUpdate(task.getRegisterStatus()));
        listVO.setCreateTime(DateUtils.mmLongTimeToStringTime(task.getCtime()));
        listVO.setUpdateTime(DateUtils.mmLongTimeToStringTime(task.getUtime()));
        if (Objects.isNull(listVO.getDesc())) {
            listVO.setDesc("");
        }
        if (Objects.isNull(listVO.getChannelName())) {
            listVO.setChannelName("");
        }
        return listVO;
    }

    @Transactional
    public DeviceAccountTaskListVO updateTask(TaskDTO taskDTO) {
        DeviceAccountTask task = deviceAccountTaskMapper.findById(taskDTO.getTaskId()).orElse(null);
        if (Objects.isNull(task)) {
            throw new BizException("任务不存在");
        }
        if (StringUtils.equals("delete", taskDTO.getPhoneNum())) {
            if (!TaskEngine.taskCanDelete(task)) {
                throw new BizException("该接码任务状态，不可以删除!刷新页面~");
            }
            task.setDeliverStatus(DeviceAccountTaskStatusEnum.ARTI_DELETED.getCode());
            deviceAccountTaskMapper.save(task);
            return convert(task);
        }
        if (!TaskEngine.taskCanOperate(task)) {
            throw new BizException("该接码任务状态，不可以操作!刷新页面~");
        }
//        validatePhoneNum(taskDTO,task.getCompetitorCode());
        TaskEngine.publishTaskEvent(task);
        TaskEngine.onUserOperate(task, taskDTO);
        deviceAccountTaskMapper.save(task);
        return convert(task);
    }

    @Transactional
    public DeviceAccountTaskListVO updateChannel(TaskDTO taskDTO) {
        DeviceAccountTask task = deviceAccountTaskMapper.findById(taskDTO.getTaskId()).orElse(null);
        if (Objects.isNull(task)) {
            throw new BizException("任务不存在。");
        }

        if (StringUtils.isNotEmpty(task.getChannelName())) {
            throw new BizException("已经修录入过渠道名称了，不能在修改渠道名称。");
        }
        task.setChannelName(taskDTO.getChannelName());
        task.setUtime(System.currentTimeMillis());
        deviceAccountTaskMapper.save(task);
        return convert(task);
    }

//    private void validatePhoneNum(TaskDTO taskDTO,String competitorCode) {
//        boolean validatePhone = configService.validatePhone();
//        if (!validatePhone) {
//            return;
//        }
//        boolean phoneNumUsed = phoneNumUsed(taskDTO.getPhoneNum(), taskDTO.getTaskId(),competitorCode);
//        if (phoneNumUsed) {
//            log.warn("手机号已拉黑/已使用, request:[{}]", JSON.toJSONString(taskDTO));
//            throw new BizException("该手机号不可用，请更换手机号");
//        }
//    }

//    private boolean phoneNumUsed(String phoneNum, Long taskId,String competitorCode) {
//        UsedPhoneNum usedPhoneNum = new UsedPhoneNum();
//        usedPhoneNum.setPhoneNum(phoneNum);
//        List<UsedPhoneNum> usedPhoneNumList = usedPhoneNumMapper.findAll(Example.of(usedPhoneNum));
//        if (CollectionUtils.isNotEmpty(usedPhoneNumList)) {
//            return true;
//        }
//        DeviceAccountTask task = new DeviceAccountTask();
//        task.setPhoneNum(phoneNum);
//        // 接码成功的手机号不可以复用，其他的还可以
//        task.setRegisterStatus(DeviceAccountTaskStatusEnum.RECEIVE_SUCCESS.getCode());
//        List<DeviceAccountTask> taskList = deviceAccountTaskMapper.findAll(Example.of(task));
//        taskList = taskList.stream()
//                .filter(it -> !it.getId().equals(taskId))
//                .collect(Collectors.toList());
//        return CollectionUtils.isNotEmpty(taskList);
//    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = WarningException.class)
    public void registerTask(RegisterTaskDTO registerTaskDTO) {
        if (Objects.isNull(registerTaskDTO)) {
            return;
        }
        DeviceAccountTask task = new DeviceAccountTask();
        // 下面四个属性是表的唯一键，可以使用findOne来处理
        Integer dt = Integer.valueOf(DateUtils.getTodayDt());
        task.setDt(dt);
        task.setLoginName(registerTaskDTO.getLoginName());
        task.setPassword(registerTaskDTO.getPassword());
        DeviceAccountTask dbTask = deviceAccountTaskMapper.findOne(Example.of(task)).orElse(null);
        if (Objects.nonNull(dbTask)) {
            log.warn("旧任务:[{}]", JSON.toJSONString(dbTask));
            if (!DeviceAccountTaskStatusEnum.canUpdate(dbTask.getDeliverStatus())) {
                log.error("该设备镜像不可再处理:[{}]", JSON.toJSONString(dbTask));
                throw new BizException("该设备镜像不可再处理");
            }
            dbTask.setDeliverStatus(TO_RECEIVE_CODE.getCode());
            dbTask.setPhoneNum(StringUtils.EMPTY);
            dbTask.setVerificationCode(StringUtils.EMPTY);
            dbTask.setUtime(System.currentTimeMillis());
            deviceAccountTaskMapper.save(dbTask);
            log.warn("该设备镜像今日已注册, dt:[{}], request:[{}]", dt, JSON.toJSONString(registerTaskDTO));
            throw new WarningException("该设备镜像今日已注册");
        }
        DeviceAccountTask toSaveTask = new DeviceAccountTask();
        toSaveTask.setDt(dt);
        toSaveTask.setRegisterStatus(TO_RECEIVE_CODE.getCode());
        toSaveTask.setDeviceCode(registerTaskDTO.getDeviceCode());
        toSaveTask.setImageCode(registerTaskDTO.getImageCode());
        toSaveTask.setCompetitorCode(registerTaskDTO.getCompetitorCode());
        toSaveTask.setAccountSource(registerTaskDTO.getAccountSource());
        toSaveTask.setPhoneNum(StringUtils.EMPTY);
        toSaveTask.setVerificationCode(StringUtils.EMPTY);
        toSaveTask.setExtInfo(StringUtils.EMPTY);
        toSaveTask.setCtime(System.currentTimeMillis());
        toSaveTask.setUtime(System.currentTimeMillis());
        deviceAccountTaskMapper.save(toSaveTask);
    }

//    public FetchResult queryPhoneNum(RegisterTaskDTO taskDTO) {
//        DeviceAccountTask task = new DeviceAccountTask();
//        task.setDt(Integer.valueOf(DateUtils.getTodayDt()));
//        task.setDeviceCode(taskDTO.getDeviceCode());
//        task.setImageCode(taskDTO.getImageCode());
//        task.setRegisterStatus(DeviceAccountTaskStatusEnum.PHONE_NUM_FILLED.getCode());
//        Optional<DeviceAccountTask> one = deviceAccountTaskMapper.findOne(Example.of(task));
//        return one.map(it -> FetchResult.ofPhoneNum(it.getPhoneNum())).orElse(new FetchResult());
//    }
//
//    public FetchResult queryPhoneNumBinding(RegisterTaskDTO taskDTO) {
//        DeviceAccountTask task = new DeviceAccountTask();
//        task.setDeviceCode(taskDTO.getDeviceCode());
//        task.setImageCode(taskDTO.getImageCode());
//        task.setCompetitorCode(taskDTO.getCompetitorCode());
//        Optional<DeviceAccountTask> one = deviceAccountTaskMapper.findOne(Example.of(task));
//        return one.map(it -> FetchResult.ofPhoneNum(it.getPhoneNum())).orElse(new FetchResult());
//    }
//
//    public FetchResult queryVerificationCode(FetchRequest fetchRequest) {
//        DeviceAccountTask task = new DeviceAccountTask();
//        task.setDt(Integer.valueOf(DateUtils.getTodayDt()));
//        task.setDeviceCode(fetchRequest.getDeviceCode());
//        task.setImageCode(fetchRequest.getImageCode());
//        task.setPhoneNum(fetchRequest.getPhoneNum());
//        task.setRegisterStatus(DeviceAccountTaskStatusEnum.VERIFICATION_CODE_FILLED.getCode());
//        Optional<DeviceAccountTask> one = deviceAccountTaskMapper.findOne(Example.of(task));
//        return one.map(it -> FetchResult.of(it.getPhoneNum(), it.getVerificationCode())).orElse(new FetchResult());
//    }
//
//    @Transactional
//    public void updateTaskStatus(@NotNull UpdateTaskStatusRequest request) {
//        DeviceAccountTask task = new DeviceAccountTask();
//        // 下面四个属性是表的唯一键，可以使用findOne来处理
//        task.setDt(Integer.valueOf(DateUtils.getTodayDt()));
//        task.setDeviceCode(request.getDeviceCode());
//        task.setImageCode(request.getImageCode());
//        task.setPhoneNum(request.getPhoneNum());
//        DeviceAccountTask dbTask = deviceAccountTaskMapper.findOne(Example.of(task)).orElse(null);
//        if (Objects.isNull(dbTask)) {
//            log.error("找不到对应任务, request:[{}]", JSON.toJSONString(request));
//            throw new BizException("找不到对应任务");
//        }
//        if (Objects.equals(dbTask.getRegisterStatus(), request.getStatus())) {
//            throw new WarningException("状态已和当前一致");
//        }
//        //若接码成功，则保存在account_record表中，与其他账号使用渠道 去重
//        if (Objects.equals(request.getStatus(), DeviceAccountTaskStatusEnum.RECEIVE_SUCCESS.getCode())) {
//            AddRecordDTO addRecordDTO = createAddRecordDTO(dbTask, request);
//            accountRecordService.insertRecord(addRecordDTO);
//        }
//        dbTask.setRegisterStatus(request.getStatus());
//        dbTask.setDesc(request.getDesc());
//        deviceAccountTaskMapper.save(dbTask);
//        if(dbTask.getAccountSource().equals(AccountSourceEnum.ORANGE.getDesc())){
//            return;
//        }
//        AccountDetail accountDetail = accountDetailMapper.findByPhoneNumAndCompetitorCodeAndAccountSource(dbTask.getPhoneNum(), dbTask.getCompetitorCode(), dbTask.getAccountSource());
//        if (Objects.nonNull(accountDetail)) {
//            AccountRegisterStatusEnum accountRegisterStatusEnum = AccountRegisterStatusEnum.fromTaskStatus(DeviceAccountTaskStatusEnum.getByCode(dbTask.getRegisterStatus()));
//            if (Objects.nonNull(dbTask.getDesc()) && (dbTask.getDesc().contains("风控") || dbTask.getDesc().contains("错误"))) {
//                //设置为废弃
//                accountRegisterStatusEnum = AccountRegisterStatusEnum.INVALID;
//            }
//            accountDetailMapper.updateById(accountDetail.getId(), accountRegisterStatusEnum.getStatus());
//        } else {
//            log.warn("找不到对应手机号, request:[{}]", JSON.toJSONString(request));
////            throw new WarningException("找不到对应手机号");
//        }
//    }

    // 构建添加记录对象
    private AddRecordDTO createAddRecordDTO(DeviceAccountTask dbTask, UpdateTaskStatusRequest request) {
        AddRecordDTO addRecordDTO = new AddRecordDTO();
        BeanUtils.copyProperties(dbTask, addRecordDTO);
        BeanUtils.copyProperties(request, addRecordDTO);
        addRecordDTO.setDeviceId(Long.valueOf(request.getDeviceCode()));
        addRecordDTO.setAccountCode(dbTask.getPhoneNum());
        addRecordDTO.setLoginHandle("{}");
        addRecordDTO.setExpiresTime(0L);
        addRecordDTO.setAccountTime(0L);
        addRecordDTO.setRegisterTime(0L);
        addRecordDTO.setLoginTime(0L);
        addRecordDTO.setProductCode(dbTask.getCompetitorCode());
        addRecordDTO.setLoginChannel(0);
        return addRecordDTO;
    }


    public void createTask(TaskQuery taskQuery) {
            // 创建新任务的业务逻辑
            DeviceAccountTask task = new DeviceAccountTask();
            task.setTaskType(request.getTaskType());
            task.setAccount(request.getLoginName());
            task.setPassword(request.getPassword());
            task.setVerificationCode(request.getVerificationCode());
            task.setPrice(request.getPrice());
            task.setCreateTime(new Date());
            task.setStatus(1); // 待处理状态

            // 保存到数据库
            deviceAccountTaskRepository.save(task);

    }
}
