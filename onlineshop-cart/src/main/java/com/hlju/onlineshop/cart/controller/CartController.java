package com.hlju.onlineshop.cart.controller;

import com.hlju.common.constant.AuthConstant;
import com.hlju.common.constant.CartConstant;
import com.hlju.onlineshop.cart.dto.CartDTO;
import com.hlju.onlineshop.cart.dto.CartItemDTO;
import com.hlju.onlineshop.cart.dto.UserInfoDTO;
import com.hlju.onlineshop.cart.interceptor.CartInterceptor;
import com.hlju.onlineshop.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 20:51
 */
@Controller
public class CartController {

    @Autowired
    CartService cartService;

    /**
     * 浏览器有一个user-key，标识用户身份，一个月后过期
     * 第一次访问，获分配user-key，以后每次访问都会带上
     */
    @GetMapping({"/cartList.html", "/"})
    public String cartListPage(Model model) {
        CartDTO cart = cartService.getCart();
        model.addAttribute("cart", cart);

        return "cartList";
    }

    /**
     * 添加商品到购物车
     *
     * @param skuId 商品id
     * @param num   数量
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes redirectAttributes) {
        cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);

        return "redirect:http://cart.onlineshop.hlju.com/success.html";
    }

    @GetMapping("/success.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,
                                       Model model) {
        CartItemDTO cartItemDTO = cartService.getCartItem(skuId);
        model.addAttribute("cartItem", cartItemDTO);
        return "success";
    }

    @GetMapping("/changeItemChecked")
    public String changeItemChecked(@RequestParam("skuId") Long skuId,
                                    @RequestParam("checked") Boolean checked) {
        cartService.changeItemChecked(skuId, checked);
        return "redirect:http://cart.onlineshop.hlju.com/";
    }

    @GetMapping("/changeItemNum")
    public String changeItemNum(@RequestParam("skuId") Long skuId,
                                @RequestParam("num") Integer num) {
        cartService.changeItemNum(skuId, num);
        return "redirect:http://cart.onlineshop.hlju.com/";
    }

    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId) {
        cartService.deleteItem(skuId);
        return "redirect:http://cart.onlineshop.hlju.com/";
    }

}
