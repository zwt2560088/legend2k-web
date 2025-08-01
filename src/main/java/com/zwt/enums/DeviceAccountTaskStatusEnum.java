package com.zwt.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Date: 2024/5/10 16:38
 */
@AllArgsConstructor
@Getter
public enum DeviceAccountTaskStatusEnum {

    TO_RECEIVE_CODE(1, "待履约"),
    PHONE_NUM_FILLED(2, "已登陆"),
    VERIFICATION_CODE_TRIGGERED(3, "验证码已发送"),
    VERIFICATION_CODE_FILLED(4, "已登陆"),
    LOGIN_SUCCESS(5, "登陆成功"),
    RECEIVE_FAIL(6, "接码失败(需要更换手机号)"),
    TASK_DROP(7, "该接码任务放弃"),
    UNGET_VERIFICATION(8, "收不到验证码"),
    ARTI_DELETED(9, "人工已删除");

    private int code;

    private String desc;

    private static final Map<Integer, DeviceAccountTaskStatusEnum> STATUS_MAP = Maps.newHashMap();

    private static final Set<Integer> CAN_OPERATE_CODE_SET =
            Sets.newHashSet(TO_RECEIVE_CODE.code, VERIFICATION_CODE_TRIGGERED.code, RECEIVE_FAIL.code);

    private static final Set<Integer> CAN_DELETE_CODE_SET =
            Sets.newHashSet(TO_RECEIVE_CODE.code, PHONE_NUM_FILLED.code, VERIFICATION_CODE_TRIGGERED.code, VERIFICATION_CODE_FILLED.code, RECEIVE_FAIL.code);

    private static final Set<Integer> DONE_STATUS_VALUE_SET =
            Sets.newHashSet(RECEIVE_SUCCESS.code, TASK_DROP.code, UNGET_VERIFICATION.code, ARTI_DELETED.code);

    static {
        Arrays.stream(values())
                .forEach(it -> STATUS_MAP.put(it.getCode(), it));
    }

    public static String getDescByCode(Integer code) {
        return Optional.ofNullable(STATUS_MAP.get(code))
                .map(DeviceAccountTaskStatusEnum::getDesc)
                .orElse(StringUtils.EMPTY);
    }

    public static DeviceAccountTaskStatusEnum getByCode(Integer code) {
        return STATUS_MAP.get(code);
    }

    public static boolean canOperateByUser(Integer status) {
        return CAN_OPERATE_CODE_SET.contains(status);
    }

    public static boolean canDeleteByUser(Integer status) {
        return CAN_DELETE_CODE_SET.contains(status);
    }

    public static boolean canUpdate(Integer status) {
        return !DONE_STATUS_VALUE_SET.contains(status);
    }


}
