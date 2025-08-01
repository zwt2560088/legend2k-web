package com.zwt.model.account;

import com.smart.disguiser.agent.disguiser.agent.model.common.Page;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Date: 2024/5/27 16:29
 */
@Data
public class QueryRecordDTO {

    private Integer startDt;

    private Integer endDt;

    @NotBlank(message = "竞对编码不能为空")
    private String competitorCode;

    @NotNull(message = "登录渠道不能为空")
    private Integer loginChannel;

    @NotNull(message = "分页信息必填")
    private Page page;

}
