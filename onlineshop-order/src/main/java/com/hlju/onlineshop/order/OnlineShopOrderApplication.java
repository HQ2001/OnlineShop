package com.hlju.onlineshop.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableFeignClients
@MapperScan("com.hlju.onlineshop.order.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class OnlineShopOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopOrderApplication.class, args);
    }

}
