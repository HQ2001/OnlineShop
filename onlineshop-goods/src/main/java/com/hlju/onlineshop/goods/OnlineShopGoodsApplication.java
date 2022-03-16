package com.hlju.onlineshop.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.hlju.onlineshop.goods.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class OnlineShopGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopGoodsApplication.class, args);
    }

}
