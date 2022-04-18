package com.hlju.onlineshop.goods.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/18 22:53
 */
@ConfigurationProperties(prefix = "onlineshop.thread")
@Component
@Data
public class ThreadPoolConfigProperties {

    private Integer coreSize = 50;
    private Integer maxSize = 200;
    private Integer keepAliveTime = 10;

}
