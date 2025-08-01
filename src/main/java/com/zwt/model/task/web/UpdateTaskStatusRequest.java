package com.zwt.model.task.web;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Date: 2024/5/13 15:34
 */
@Data
public class UpdateTaskStatusRequest {

    @NotBlank(message = "设备号不能为空")
    private String deviceCode;

    @NotBlank(message = "镜像编号不能为空")
    private String imageCode;

//    @NotBlank(message = "手机号不能为空")
    private String phoneNum;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private String desc;
}
