package com.zwt.repo.sqlite.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author:
 * @Date: 2024/5/28 10:22
 */
@Data
@Entity
@Table(name = "account_record")
public class AccountRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountCode;

    private String competitorCode;

    private Integer loginChannel;

    private Long accountTime;

    /**
     * 保存的JSON字段
     */
    private String loginHandle;

    private Long registerTime;

    private Long loginTime;

    private Long deviceId;

    private Long expiresTime;

    private String productCode;

    private Long ctime;

    private Long utime;

}
