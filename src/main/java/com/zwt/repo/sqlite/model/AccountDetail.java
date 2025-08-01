package com.zwt.repo.sqlite.model;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "account_detail")
public class AccountDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // COM 四方用
    private Integer com;

    private String phoneNum;

    private Integer registerStatus;

    private String extInfo;

    // 渠道编码 1: IOS WX 2:Android WX  3: IOS APP  4: Android APP
    private String competitorCode;
    // 供应商来源
    private String accountSource;

    private String api;

    private String batch;

    private String expirationTime;

    private Long ctime;

    private Long utime;
}