package com.hlju.onlineshop.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.hlju.onlineshop.user.dao")
@EnableFeignClients(basePackages = "com.hlju.onlineshop.user.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class OnlineShopUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopUserApplication.class, args);
    }

}
