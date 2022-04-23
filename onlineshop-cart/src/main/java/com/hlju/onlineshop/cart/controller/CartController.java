package com.hlju.onlineshop.cart.controller;

import com.hlju.common.constant.AuthConstant;
import com.hlju.common.constant.CartConstant;
import com.hlju.onlineshop.cart.dto.UserInfoDTO;
import com.hlju.onlineshop.cart.interceptor.CartInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 20:51
 */
@Controller
public class CartController {

    /**
     * 浏览器有一个user-key，标识用户身份，一个月后过期
     * 第一次访问，获分配user-key，以后每次访问都会带上
     */
    @GetMapping({"/cartList.html", "/"})
    public String cartListPage() {
        UserInfoDTO userInfoDTO = CartInterceptor.threadLocal.get();

        return "cartList";
    }

    /**
     * 添加商品到购物车
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart() {
        UserInfoDTO userInfoDTO = CartInterceptor.threadLocal.get();

        return "success";
    }

}
