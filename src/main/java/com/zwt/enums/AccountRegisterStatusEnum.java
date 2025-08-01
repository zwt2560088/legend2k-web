package com.zwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum AccountRegisterStatusEnum {
    //根据 1初始化，2接码中，3接码成功，4接码失败 来分配任务的流转方向
    INITIALIZED(1),//orange
    RECEIVING(2),
    RECEIVE_SUCCESS(3),
    RECEIVE_FAIL(4),
    INVALID(5);
    private Integer status;

    public static AccountRegisterStatusEnum fromTaskStatus(DeviceAccountTaskStatusEnum originalStatus) {
        switch (originalStatus) {
            case TO_RECEIVE_CODE:
                return INITIALIZED;
            case PHONE_NUM_FILLED:
            case VERIFICATION_CODE_TRIGGERED:
            case VERIFICATION_CODE_FILLED:
                return RECEIVING;
            case RECEIVE_SUCCESS:
                return RECEIVE_SUCCESS;
            case RECEIVE_FAIL:
            case TASK_DROP:
            case UNGET_VERIFICATION:
            case ARTI_DELETED:
                return RECEIVE_FAIL;
            default:
                throw new IllegalArgumentException("Unknown status: " + originalStatus);
        }

    }
}
