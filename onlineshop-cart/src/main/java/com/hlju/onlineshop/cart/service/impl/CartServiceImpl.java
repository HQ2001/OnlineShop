package com.hlju.onlineshop.cart.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 20:48
 */
@Slf4j
@Service("CartService")
public class CartServiceImpl {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public CartServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
