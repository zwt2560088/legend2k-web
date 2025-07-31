package com.zwt.controller;

import com.smart.disguiser.agent.disguiser.agent.aop.MethodLog;
import com.smart.disguiser.agent.disguiser.agent.model.ResultDTO;
import com.smart.disguiser.agent.disguiser.agent.model.task.web.*;
import com.smart.disguiser.agent.disguiser.agent.service.account.DeviceAccountTaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Date: 2024/5/13 10:14
 */
@RestController
@RequestMapping("/accountTask/out")
public class DeviceAccountTaskOutController {

    @Resource
    private DeviceAccountTaskService taskService;

    @MethodLog(value = "注册接码任务", httpRequest = true)
    @PostMapping("/register")
    public ResultDTO registerTask(@Validated @RequestBody RegisterTaskDTO taskDTO) {
        taskService.registerTask(taskDTO);
        return ResultDTO.success();
    }

    @MethodLog(value = "查询手机号", httpRequest = true)
    @PostMapping("/queryPhoneNum")
    public ResultDTO<FetchResult> queryPhoneNum(@Validated @RequestBody RegisterTaskDTO taskDTO) {
        return ResultDTO.success(taskService.queryPhoneNum(taskDTO));
    }
    @MethodLog(value = "查询镜像绑定的手机号(拉黑时使用)", httpRequest = true)
    @PostMapping("/queryPhoneNumBinding")
    public ResultDTO<FetchResult> queryPhoneNumBinding(@Validated @RequestBody RegisterTaskDTO taskDTO) {
        return ResultDTO.success(taskService.queryPhoneNumBinding(taskDTO));
    }

    @MethodLog(value = "查询验证码", httpRequest = true)
    @PostMapping("/queryVerificationCode")
    public ResultDTO<FetchResult> queryVerificationCode(@Validated @RequestBody FetchRequest fetchRequest) {
        return ResultDTO.success(taskService.queryVerificationCode(fetchRequest));
    }

    @MethodLog(value = "更新接码任务状态", httpRequest = true)
    @PostMapping("/updateTaskStatus")
    public ResultDTO updateTaskStatus(@Validated @RequestBody UpdateTaskStatusRequest request) {
        taskService.updateTaskStatus(request);
        return ResultDTO.success();
    }

}
