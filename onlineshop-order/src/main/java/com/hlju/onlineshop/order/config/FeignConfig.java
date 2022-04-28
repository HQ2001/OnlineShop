package com.hlju.onlineshop.order.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/26 12:15
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (template) -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            // 原请求
            HttpServletRequest request = attributes.getRequest();
            // 同步请求头
            String cookieStr = "Cookie";
            String cookie = request.getHeader(cookieStr);
            template.header(cookieStr, cookie);
        };
    }

}
