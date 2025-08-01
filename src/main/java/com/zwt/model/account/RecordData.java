package com.zwt.model.account;

import lombok.Data;

/**
 * @Author:
 * @Date: 2024/5/27 16:31
 */
@Data
public class RecordData {

    private Long id;

    private String accountCode;

    private String competitorCode;

    private Integer loginChannel;

    private Long accountTime;

    private String loginHandle;

    private Long registerTime;

    private Long loginTime;

    private Long deviceId;

    private Long expiresTime;

    private String productCode;

}
