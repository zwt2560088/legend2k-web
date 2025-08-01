package com.zwt.model.task.web;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Date: 2024/5/11 14:53
 */
@Data
public class TaskDTO {

    @NotNull(message = "ID不能为空")
    private Long taskId;

//    @NotBlank(message = "手机号不能为空")
    private String phoneNum;

    private String verificationCode;

    /**
     * 码商 内部分渠道
     */
    private String channelName;

}
