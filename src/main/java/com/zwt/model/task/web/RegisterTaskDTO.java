package com.zwt.model.task.web;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author:
 * @Date: 2024/5/13 10:25
 */
@Data
public class RegisterTaskDTO {

    private String taskType;

    private String loginName;

    private String password;

    //@NotBlank(message = "竞对不能为空")
    private String verificationCode;

    // 扩展信息
    private String extInfo;

    private String gameType;

    private String supplier;

}
