package com.hlju.onlineshop.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.hlju.onlineshop.coupon.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class OnlineShopCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopCouponApplication.class, args);
    }

}
