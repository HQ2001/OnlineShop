package com.hlju.onlineshop.cart.service;

import com.hlju.onlineshop.cart.dto.CartDTO;
import com.hlju.onlineshop.cart.dto.CartItemDTO;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 20:47
 */
public interface CartService {


    /**
     * 向购物车中添加数据
     * @param skuId skuId
     * @param num 数量
     */
    void addToCart(Long skuId, Integer num);

    /**
     * 获取购物项
     * @param skuId skuId
     * @return dto
     */
    CartItemDTO getCartItem(Long skuId);

    /**
     * 获取当前登录用户的购物车
     * @return dto
     */
    CartDTO getCart();

    /**
     * 变换购物项是否选中
     * @param skuId skuId
     * @param checked 是否选中
     */
    void changeItemChecked(Long skuId, Boolean checked);

    /**
     * 变换购物项数量
     * @param skuId skuId
     * @param num 数量
     */
    void changeItemNum(Long skuId, Integer num);

    /**
     * 删除购物项
     * @param skuId skuId
     */
    void deleteItem(Long skuId);

    /**
     * 获取最新的已选中的购物项
     * @return
     */
    List<CartItemDTO> getUpToDateCheckedCartItems(Long userId);
}
