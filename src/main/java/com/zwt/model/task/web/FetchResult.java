package com.zwt.model.task.web;

import lombok.Data;

/**
 * @Date: 2024/5/13 15:10
 */
@Data
public class FetchResult {

    private String phoneNum;

    private String verificationCode;

    public static FetchResult ofPhoneNum(String phoneNum) {
        FetchResult result = new FetchResult();
        result.setPhoneNum(phoneNum);
        return result;
    }

    public static FetchResult ofCode(String code) {
        FetchResult result = new FetchResult();
        result.setVerificationCode(code);
        return result;
    }

    public static FetchResult of(String phoneNum, String code) {
        FetchResult result = new FetchResult();
        result.setPhoneNum(phoneNum);
        result.setVerificationCode(code);
        return result;
    }



}
