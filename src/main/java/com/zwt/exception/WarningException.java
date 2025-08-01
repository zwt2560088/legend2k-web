package com.zwt.exception;

import com.smart.disguiser.agent.disguiser.agent.enums.ResponseStatusEnum;
import lombok.Data;

/**
 * 数据一致性校验不通过时抛出，不需要调用方重试
 */
@Data
public class WarningException extends RuntimeException {

    private int code = ResponseStatusEnum.SUCCESS.getCode();

    private String message;

    public WarningException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public WarningException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public WarningException(String message) {
        super(message);
        this.message = message;
    }

    public WarningException(ResponseStatusEnum responseStatusEnum) {
        super(responseStatusEnum.getMsg());
        this.code = responseStatusEnum.getCode();
        this.message = responseStatusEnum.getMsg();
    }
}