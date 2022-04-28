package com.hlju.onlineshop.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/26 9:50
 */
@Data
public class OrderItemVO {

    /**
     * 商品id
     */
    private Long skuId;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */
    private String image;

    /**
     * 属性
     */
    private List<String> skuAttrs;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 是否有货
     */
    // TODO 查询是否有库存
    private Boolean hasStock = true;

    /**
     * 获取总价
     */
    public BigDecimal getTotalPrice() {
        return this.price.multiply(new BigDecimal(String.valueOf(this.count)));
    }

}
