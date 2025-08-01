package com.zwt.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date: 2024/5/14 11:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskChangePhoneEvent {

    private String phoneNum;

}
