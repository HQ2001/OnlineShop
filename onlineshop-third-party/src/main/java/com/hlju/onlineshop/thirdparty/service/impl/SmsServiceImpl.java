package com.hlju.onlineshop.thirdparty.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.hlju.onlineshop.thirdparty.config.AliCloudConfig;
import com.hlju.onlineshop.thirdparty.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/21 10:19
 */
@Slf4j
@Service("SmsService")
public class SmsServiceImpl implements SmsService {

    private final Client client;
    private final AliCloudConfig.SmsConfig smsConfig;

    @Autowired
    SmsServiceImpl(AliCloudConfig aliCloudConfig,
                   AliCloudConfig.SmsConfig smsConfig) throws Exception {
        String accessKeyId = aliCloudConfig.getAccessKey();
        String accessKeySecret = aliCloudConfig.getSecretKey();
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = smsConfig.getEndpoint();
        this.smsConfig = smsConfig;
        this.client = new Client(config);
    }

    @Override
    public void sendSmsCode(String phone, String code) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(smsConfig.getSignName())
                .setTemplateCode(smsConfig.getTemplateCode())
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\"" + code + "\"}");
        try {
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            log.info("send sms : header{}, body{}", sendSmsResponse.getHeaders(), sendSmsResponse.getBody().getMessage());
        } catch (Exception e) {
            log.error("短信发送失败", e);
        }
    }
}
