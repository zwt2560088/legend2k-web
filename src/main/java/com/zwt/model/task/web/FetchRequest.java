package com.zwt.model.task.web;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author:
 * @Date: 2024/5/13 10:25
 */
@Data
public class FetchRequest {

    @NotBlank(message = "设备号不能为空")
    private String deviceCode;

    @NotBlank(message = "镜像编号不能为空")
    private String imageCode;

    @NotBlank(message = "手机号不能为空")
    private String phoneNum;

}
