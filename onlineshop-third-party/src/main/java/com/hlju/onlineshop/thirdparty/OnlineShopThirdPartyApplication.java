package com.hlju.onlineshop.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class OnlineShopThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopThirdPartyApplication.class, args);
    }

}
