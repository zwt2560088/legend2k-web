package com.zwt.exception;

import com.smart.disguiser.agent.disguiser.agent.enums.ResponseStatusEnum;
import lombok.Data;

/**
 * @Date: 2021/10/31 21:38
 * @Description:
 */
@Data
public class BizException extends RuntimeException {

    private int code = ResponseStatusEnum.PARAM_ERROR.getCode();

    private String message;

    public BizException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(String message) {
        super(message);
        this.message = message;
    }

    public BizException(ResponseStatusEnum responseStatusEnum) {
        super(responseStatusEnum.getMsg());
        this.code = responseStatusEnum.getCode();
        this.message = responseStatusEnum.getMsg();
    }
}