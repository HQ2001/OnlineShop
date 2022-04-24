package com.hlju.onlineshop.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.hlju.common.constant.CartConstant;
import com.hlju.common.to.SkuInfoTO;
import com.hlju.common.utils.R;
import com.hlju.onlineshop.cart.dto.CartDTO;
import com.hlju.onlineshop.cart.dto.CartItemDTO;
import com.hlju.onlineshop.cart.dto.UserInfoDTO;
import com.hlju.onlineshop.cart.feign.GoodsFeignService;
import com.hlju.onlineshop.cart.interceptor.CartInterceptor;
import com.hlju.onlineshop.cart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 20:48
 */
@Slf4j
@Service("CartService")
public class CartServiceImpl implements CartService {

    private final StringRedisTemplate redisTemplate;
    private final GoodsFeignService goodsFeignService;
    private final ThreadPoolExecutor executor;

    @Autowired
    public CartServiceImpl(StringRedisTemplate redisTemplate,
                           GoodsFeignService goodsFeignService,
                           ThreadPoolExecutor executor) {
        this.redisTemplate = redisTemplate;
        this.goodsFeignService = goodsFeignService;
        this.executor = executor;
    }

    @Override
    public void addToCart(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String cart = (String) cartOps.get(String.valueOf(skuId));
        // 购物车中有此商品，只需要增加数量
        if (StringUtils.isNotEmpty(cart)) {
            CartItemDTO cartItemDTO = JSON.parseObject(cart, CartItemDTO.class);
            cartItemDTO.setCount(cartItemDTO.getCount() + num);
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItemDTO));
        }
        // sku基本信息
        CountDownLatch countDownLatch = new CountDownLatch(2);
        CartItemDTO cartItem = new CartItemDTO();
        CompletableFuture.runAsync(() -> {
            R info = goodsFeignService.info(skuId);
            SkuInfoTO skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoTO>() {
            });
            cartItem.setSkuId(skuId);
            cartItem.setCount(num);
            cartItem.setImage(skuInfo.getSkuDefaultImg());
            cartItem.setTitle(skuInfo.getSkuTitle());
            cartItem.setPrice(skuInfo.getPrice());
            countDownLatch.countDown();
        }, executor);

        // sku的组合信息
        CompletableFuture.runAsync(() -> {
            R r = goodsFeignService.getSkuSaleAttrValues(skuId);
            List<String> skuAttrs = r.getData("list", new TypeReference<List<String>>() {
            });
            cartItem.setSkuAttrs(skuAttrs);
            countDownLatch.countDown();
        }, executor);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    @Override
    public CartItemDTO getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String cart = (String) cartOps.get(String.valueOf(skuId));
        return JSON.parseObject(cart, CartItemDTO.class);
    }

    @Override
    public CartDTO getCart() {
        CartDTO cart = new CartDTO();
        UserInfoDTO userInfoDTO = CartInterceptor.threadLocal.get();
        String tempCartKey = CartConstant.CART_PREFIX + userInfoDTO.getUserKey();
        List<CartItemDTO> tempCartItems = this.getCartItemsByCartKey(tempCartKey);
        if (Objects.nonNull(userInfoDTO.getUserId())) {
            // 已登录，合并临时购物车到登录购物车中，并且删除临时购物车
            if (CollectionUtils.isNotEmpty(tempCartItems)) {
                for (CartItemDTO tempCartItem : tempCartItems) {
                    addToCart(tempCartItem.getSkuId(), tempCartItem.getCount());
                }
            }
            String cartKey = CartConstant.CART_PREFIX + userInfoDTO.getUserId();
            List<CartItemDTO> cartItems = this.getCartItemsByCartKey(cartKey);
            cart.setCartItems(cartItems);
            redisTemplate.delete(tempCartKey);
        } else {
            // 未登录，直接查询临时购物车
            cart.setCartItems(tempCartItems);
        }
        return cart;
    }

    @Override
    public void changeItemChecked(Long skuId, Boolean checked) {
        CartItemDTO cartItem = getCartItem(skuId);
        cartItem.setChecked(checked);
        String cartItemJson = JSON.toJSONString(cartItem);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(), cartItemJson);
    }

    @Override
    public void changeItemNum(Long skuId, Integer num) {
        CartItemDTO cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        String cartItemJson = JSON.toJSONString(cartItem);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(), cartItemJson);
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    /**
     * 获取要操作的购物车
     *
     * @return 当前操作的购物车
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoDTO userInfoDTO = CartInterceptor.threadLocal.get();
        String cartKey;
        if (Objects.nonNull(userInfoDTO.getUserId())) {
            cartKey = CartConstant.CART_PREFIX + userInfoDTO.getUserId();
        } else {
            cartKey = CartConstant.CART_PREFIX + userInfoDTO.getUserKey();
        }

        return redisTemplate.boundHashOps(cartKey);
    }

    /**
     * 根据购物车的key查询购物车中的所有购物项
     *
     * @param cartKey 购物车的key
     * @return 购物项list
     */
    private List<CartItemDTO> getCartItemsByCartKey(String cartKey) {
        BoundHashOperations<String, Object, Object> cartOps = redisTemplate.boundHashOps(cartKey);
        List<Object> values = cartOps.values();
        if (CollectionUtils.isNotEmpty(values)) {
            return values.stream()
                    .map(item -> {
                        String itemStr = String.valueOf(item);
                        return JSON.parseObject(itemStr, CartItemDTO.class);
                    }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }
}
