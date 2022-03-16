package com.hlju.onlineshop.warehouse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.hlju.onlineshop.warehouse.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class OnlineShopWarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopWarehouseApplication.class, args);
    }

}
