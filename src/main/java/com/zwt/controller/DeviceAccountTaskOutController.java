package com.zwt.controller;


import com.zwt.model.task.web.FetchRequest;
import com.zwt.model.task.web.FetchResult;
import com.zwt.model.task.web.RegisterTaskDTO;
import com.zwt.model.task.web.UpdateTaskStatusRequest;
import com.zwt.repo.ResultDTO;
import com.zwt.service.DeviceAccountTaskService;
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

    @PostMapping("/register")
    public ResultDTO registerTask(@Validated @RequestBody RegisterTaskDTO taskDTO) {
        taskService.registerTask(taskDTO);
        return ResultDTO.success();
    }

//    @PostMapping("/queryPhoneNum")
//    public ResultDTO<FetchResult> queryPhoneNum(@Validated @RequestBody RegisterTaskDTO taskDTO) {
//        return ResultDTO.success(taskService.queryPhoneNum(taskDTO));
//    }
//
//    @PostMapping("/queryPhoneNumBinding")
//    public ResultDTO<FetchResult> queryPhoneNumBinding(@Validated @RequestBody RegisterTaskDTO taskDTO) {
//        return ResultDTO.success(taskService.queryPhoneNumBinding(taskDTO));
//    }
//
//
//    @PostMapping("/queryVerificationCode")
//    public ResultDTO<FetchResult> queryVerificationCode(@Validated @RequestBody FetchRequest fetchRequest) {
//        return ResultDTO.success(taskService.queryVerificationCode(fetchRequest));
//    }
//
//    @PostMapping("/updateTaskStatus")
//    public ResultDTO updateTaskStatus(@Validated @RequestBody UpdateTaskStatusRequest request) {
//        taskService.updateTaskStatus(request);
//        return ResultDTO.success();
//    }

}
