package com.zwt.model.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Date: 2024/5/27 16:19
 */
@Data
public class AddRecordDTO {

    @NotBlank(message = "账户编码(手机号)不能为空")
    private String accountCode;

    @NotBlank(message = "竞对编码不能为空")
    private String competitorCode;

    private Integer loginChannel;

    @NotNull(message = "获取账户时间不能为空")
    private Long accountTime;

    @NotBlank(message = "登录会话句柄不能为空")
    private String loginHandle;

    @NotNull(message = "成功注册时间不能为空")
    private Long registerTime;

    @NotNull(message = "成功登录的时间不能为空")
    private Long loginTime;

    @NotNull(message = "设备id不能为空")
    private Long deviceId;

    @NotNull(message = "账户失效时间不能为空")
    private Long expiresTime;

    @NotBlank(message = "供应商产品编码不能为空")
    private String productCode;

}
