package com.zwt.repo.sqlite.model;

import lombok.Data;

import javax.persistence.*;

/**
 *
 * @description
 * @date 2024/5/30 15:35
 */
@Data
@Entity
@Table(name = "device_data")
public class DeviceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 竞对类型
    private String cpt;

    // 渠道编码 1: IOS WX 2:Android WX  3: IOS APP  4: Android APP
    private Integer channelCode;

    private Long deviceId;

    private String deviceData;

    // 使用状态
    private Integer useStatus;

    private Integer status;

    private String extInfo;

    private Long ctime;

    private Long utime;
}
