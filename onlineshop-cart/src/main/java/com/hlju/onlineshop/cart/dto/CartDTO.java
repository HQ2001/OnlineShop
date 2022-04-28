package com.hlju.onlineshop.cart.dto;

import com.alibaba.nacos.common.utils.CollectionUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 20:19
 */
@Data
public class CartDTO {

    List<CartItemDTO> cartItems;

    /**
     * 减免价格
     */
    private BigDecimal reduce = new BigDecimal("0.00");

    /**
     * 获取商品数量
     */
    public Integer getCountNum() {
        if (CollectionUtils.isNotEmpty(cartItems)) {
            int count = 0;
            for (CartItemDTO item : cartItems) {
                count += item.getCount();
            }
            return count;
        }
        return 0;
    }

    /**
     * 获取商品种类数量
     */
    public Integer getCountTypeNum() {
        if (CollectionUtils.isNotEmpty(cartItems)) {
            return cartItems.size();
        }
        return 0;
    }

    /**
     * 所有商品总价
     */
    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartItems)) {
            for (CartItemDTO item : cartItems) {
                if (item.getChecked()) {
                    amount = amount.add(item.getTotalPrice());
                }
            }
        }
        // 减去优惠
        amount = amount.subtract(getReduce());
        return amount;
    }
}
