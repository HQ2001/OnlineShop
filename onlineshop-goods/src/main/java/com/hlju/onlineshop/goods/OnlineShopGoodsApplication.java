package com.hlju.onlineshop.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.hlju.onlineshop.goods.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class OnlineShopGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopGoodsApplication.class, args);
    }

}
