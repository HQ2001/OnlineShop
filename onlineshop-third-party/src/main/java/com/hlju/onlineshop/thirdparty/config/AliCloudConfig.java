package com.hlju.onlineshop.thirdparty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/21 10:22
 */
@ConfigurationProperties(prefix = "spring.cloud.alicloud")
@Configuration
@Data
public class AliCloudConfig {
    private String accessKey;
    private String secretKey;

    @ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
    @Configuration
    @Data
    public static class SmsConfig {
        private String signName;
        private String templateCode;
        private String endpoint;
    }
}
