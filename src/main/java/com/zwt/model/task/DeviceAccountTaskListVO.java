package com.zwt.model.task;

import lombok.Data;

/**
 * @Date: 2024/5/10 16:50
 */
@Data
public class DeviceAccountTaskListVO {

    private Long id;

    private String deviceCode;

    private String imageCode;

    private String statusValue;

    private String phoneNum;

    private String verificationCode;

    /**
     * 码商 内部分渠道
     */
    private String channelName;

    private Boolean needDeal;

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

    private String createTime;

    private String updateTime;

    private String desc;
}
