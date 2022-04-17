package com.hlju.onlineshop.goods;

import com.alibaba.fastjson.TypeReference;
import com.hlju.common.utils.R;
import com.hlju.onlineshop.goods.feign.WarehouseFeignService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class OnlineShopGoodsApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    WarehouseFeignService warehouseFeignService;

    @Test
    public void testRedisson() {
        System.out.println(redissonClient);
    }

    @Test
    public void testStringRedisTemplate() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello", "world_" + UUID.randomUUID());
        String hello = ops.get("hello");
        System.out.println(hello);
    }

    @Test
    public void GetSkusHasStockTest() {
        Map<Long, Boolean> map = warehouseFeignService.getSkusHasStock(Arrays.asList(1L, 2L, 3L, 37L)).getData(
                new TypeReference<Map<Long, Boolean>>() {
                }
        );
        System.out.println(map);
    }

}
