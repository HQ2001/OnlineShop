package com.hlju.onlineshop.thirdparty.service;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/21 10:19
 */
public interface SmsService {

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @param code 验证码
     */
    void sendSmsCode(String phone, String code);

}
