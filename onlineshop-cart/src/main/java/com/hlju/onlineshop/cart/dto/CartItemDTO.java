package com.hlju.onlineshop.cart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物项
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 20:19
 */
@Data
public class CartItemDTO {

    /**
     * 商品id
     */
    private Long skuId;

    /**
     * 是否选中，默认为true
     */
    private Boolean checked = true;

    /**
     * 图片
     */
    private String image;

    /**
     * 属性
     */
    private List<String> skuAttr;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 获取总价
     */
    public BigDecimal getTotalPrice() {
        return this.price.multiply(new BigDecimal(String.valueOf(this.count)));
    }
}
