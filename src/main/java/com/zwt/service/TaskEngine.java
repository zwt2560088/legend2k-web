package com.zwt.service;


import com.zwt.enums.DeviceAccountTaskStatusEnum;
import com.zwt.exception.BizException;
import com.zwt.model.event.TaskChangePhoneEvent;
import com.zwt.model.task.web.TaskDTO;
import com.zwt.repo.sqlite.model.DeviceAccountTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @Date: 2024/5/14 11:00
 */
@Slf4j
@Service
public class TaskEngine implements ApplicationContextAware {

    static ApplicationContext applicationContext;

    public static final String PHONE_NUM_REGEX = "^1[3-9]\\d{9}$";

    public static boolean taskCanOperate(DeviceAccountTask task) {
        return DeviceAccountTaskStatusEnum.canOperateByUser(task.getRegisterStatus());
    }

    public static boolean taskCanDelete(DeviceAccountTask task) {
        return DeviceAccountTaskStatusEnum.canDeleteByUser(task.getRegisterStatus());
    }

    public static void publishTaskEvent(DeviceAccountTask task) {
        DeviceAccountTaskStatusEnum status = DeviceAccountTaskStatusEnum.getByCode(task.getRegisterStatus());
        if (DeviceAccountTaskStatusEnum.RECEIVE_FAIL.equals(status)) {
            applicationContext.publishEvent(new TaskChangePhoneEvent(task.getPhoneNum()));
        }
    }

    public static void onUserOperate(DeviceAccountTask task, TaskDTO taskDTO) {
        DeviceAccountTaskStatusEnum status = DeviceAccountTaskStatusEnum.getByCode(task.getRegisterStatus());
        switch (status) {
            case TO_RECEIVE_CODE:
                boolean matches = taskDTO.getPhoneNum().matches(PHONE_NUM_REGEX);
                if (!matches) {
                    throw new BizException("手机号不合法，请验证后再次提交");
                }
                task.setPhoneNum(taskDTO.getPhoneNum());
                task.setRegisterStatus(DeviceAccountTaskStatusEnum.PHONE_NUM_FILLED.getCode());
                break;
            case VERIFICATION_CODE_TRIGGERED:
                if (StringUtils.isBlank(taskDTO.getVerificationCode())) {
                    throw new BizException("当前状态下提交，验证码不能为空");
                }
                task.setVerificationCode(taskDTO.getVerificationCode());
                task.setRegisterStatus(DeviceAccountTaskStatusEnum.VERIFICATION_CODE_FILLED.getCode());
                break;
            case RECEIVE_FAIL:
                task.setPhoneNum(taskDTO.getPhoneNum());
                task.setRegisterStatus(DeviceAccountTaskStatusEnum.PHONE_NUM_FILLED.getCode());
                task.setVerificationCode(StringUtils.EMPTY);
                break;
            default:
                break;
        }
        task.setUtime(System.currentTimeMillis());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TaskEngine.applicationContext = applicationContext;
    }
}
