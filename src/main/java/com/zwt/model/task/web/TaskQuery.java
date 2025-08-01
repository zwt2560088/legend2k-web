package com.zwt.model.task.web;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Date: 2024/5/10 17:16
 */
@Data
public class TaskQuery {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDt;

    /**
     * 刷新时使用
     */
    private Boolean queryAll;

    /**
     * 参考 DeviceAccountTaskStatusEnum 取值
     */
    private Integer queryStatus;

    /**
     * 竞对 例如:dd,pp
     * CptTypeEnum
     */
    private String competitorCode;

    /**
     * 码商 例如:orange,四方
     * AccountSourceEnum
     */
    private String accountSource;
    /**
     * 设备Code 例如:
     */
    private String deviceCode;

    /**
     * 镜像Code
     */
    private String imageCode;

    /**
     * orange供应商下不同分渠道
     */
    private String channelName;
}
