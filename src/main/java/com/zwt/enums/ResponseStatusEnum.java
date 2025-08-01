package com.zwt.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2021/7/28 11:50
 * @Description:统一响应状态
 */
public enum ResponseStatusEnum {
    SUCCESS(0, "成功"),
    INTERNAL_ERROR(1, "服务器内部错误"),
    PARAM_ERROR(2, "参数错误"),
    LOST_RECORD(3, "记录信息缺失"),
    LOGIC_DELETE_ERROR(4, "逻辑删除错误");

    private int code;   //状态码
    private String msg; //状态说明

    ResponseStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //构建内部静态映射
    private static Map<Integer, ResponseStatusEnum> enumMap = new HashMap<>();
    static {
        Arrays.stream(ResponseStatusEnum.values())
                .forEach(status -> enumMap.put(status.code, status));
    }

    /**
     * 根据code转换为枚举
     * @param code
     * @return
     */
    public static ResponseStatusEnum of(Integer code) {
        return enumMap.get(code);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
