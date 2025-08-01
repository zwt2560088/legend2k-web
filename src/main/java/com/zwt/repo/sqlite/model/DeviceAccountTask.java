package com.zwt.repo.sqlite.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Date: 2024/5/9 17:00
 */
@Data
@Entity
@Table(name = "device_account_task")
public class DeviceAccountTask implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer dt;

    private String taskType;

    private String loginName;

    private String password;

    private String verificationCode;

    private String price;

    /**
     * 码商 内部分渠道
     */
    private String channelName;

    private String extInfo;

    private String gameType;

    private String supplier;

    private String supplierPrice;


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Integer dt;
//
//    private String deviceCode;
//
//    private String imageCode;
//
    private Integer deliverStatus;
//
//    private String phoneNum;
//
//    private String verificationCode;
//
//    /**
//     * 码商 内部分渠道
//     */
//    private String channelName;
//
//    private String extInfo;
//
//    private String competitorCode;
//
//    private String accountSource;
//
//    private String clientType;
//
//    private Integer getPhoneRetryCount;
//
//    private Integer getVerificationRetryCount;

    private Long ctime;

    private Long utime;

    private String desc;



}
