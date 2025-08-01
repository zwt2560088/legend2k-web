package com.zwt.controller;


import com.zwt.exception.BizException;
import com.zwt.model.task.DeviceAccountTaskListVO;
import com.zwt.model.task.web.TaskDTO;
import com.zwt.model.task.web.TaskQuery;
import com.zwt.repo.ResultDTO;
import com.zwt.service.DeviceAccountTaskService;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author:
 * @Date: 2024/5/9 18:06
 */
@Controller
@RequestMapping("/accountTask")
public class DeviceAccountTaskController {

    @Resource
    private DeviceAccountTaskService deviceAccountTaskService;

    @GetMapping("/index")
    public String index(@RequestParam(required = false, defaultValue = "orange") String accountSource, Model model) {
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setQueryAll(false);
        //TODO: 根据accountSource 区分列表页
        taskQuery.setAccountSource(accountSource);
        List<DeviceAccountTaskListVO> result = deviceAccountTaskService.queryTask(taskQuery);
        // 将 accountSource 添加到模型中
        model.addAttribute("accountSource", accountSource);
        model.addAttribute("list", result);
        return "ddAccountIndex";
    }
    @PostMapping("/createTask")
    public ResultDTO createTask(@Validated @RequestBody TaskQuery taskQuery) {
        deviceAccountTaskService.createTask(taskQuery);
        return ResultDTO.success();
    }

    @PostMapping("/query")
    @ResponseBody
    public ResultDTO<List<DeviceAccountTaskListVO>> listTask(@RequestBody TaskQuery taskQuery) {
        if (Objects.nonNull(taskQuery.getQueryStatus()) && taskQuery.getQueryStatus() == -1) {// 如果是-1表示查询全部状态
            taskQuery.setQueryAll(true);
            taskQuery.setQueryStatus(null);
        }
        List<DeviceAccountTaskListVO> result = deviceAccountTaskService.queryTask(taskQuery);
        if (StringUtils.isNotEmpty(taskQuery.getCompetitorCode()) && !taskQuery.getCompetitorCode().equals("-1")) {
            result = result.stream()
                    .filter(it -> StringUtils.equals(it.getCompetitorCode(), taskQuery.getCompetitorCode()))
                    .collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(taskQuery.getAccountSource()) && !taskQuery.getAccountSource().equals("-1")) {
            result = result.stream()
                    .filter(it -> StringUtils.equals(it.getAccountSource(), taskQuery.getAccountSource()))
                    .collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(taskQuery.getChannelName())) {
            result = result.stream()
                    .filter(it -> StringUtils.equals(it.getChannelName(), taskQuery.getChannelName()))
                    .collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(taskQuery.getDeviceCode()) && !taskQuery.getDeviceCode().equals("-1")) {
            result = result.stream()
                    .filter(it -> StringUtils.equals(it.getDeviceCode(), taskQuery.getDeviceCode()))
                    .collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(taskQuery.getImageCode()) && !taskQuery.getImageCode().equals("-1")) {
            result = result.stream()
                    .filter(it -> StringUtils.equals(it.getImageCode(), taskQuery.getImageCode()))
                    .collect(Collectors.toList());
        }
        return ResultDTO.success(ListUtils.emptyIfNull(result));
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultDTO<DeviceAccountTaskListVO> updateTask(@RequestBody TaskDTO taskDTO) {
        if (StringUtils.isEmpty(taskDTO.getPhoneNum())) {
            throw new BizException("手机号不能为空!");
        }
        DeviceAccountTaskListVO taskListVO = deviceAccountTaskService.updateTask(taskDTO);
        return ResultDTO.success(taskListVO);
    }

//    @PostMapping("/updateChannel")
//    @ResponseBody
//    public ResultDTO<DeviceAccountTaskListVO> updateChannel(@RequestBody TaskDTO taskDTO) {
//        DeviceAccountTaskListVO taskListVO = deviceAccountTaskService.updateChannel(taskDTO);
//        return ResultDTO.success(taskListVO);
//    }
}
