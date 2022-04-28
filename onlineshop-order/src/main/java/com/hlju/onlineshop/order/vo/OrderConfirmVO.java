package com.hlju.onlineshop.order.vo;

import com.alibaba.nacos.common.utils.CollectionUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/26 9:44
 */
@Data
public class OrderConfirmVO {

    /**
     * 收货地址
     */
    private List<UserAddressVO> addresses;

    /**
     * 所有选中的购物项
     */
    private List<OrderItemVO> items;

    // 发票记录等。。。

    /**
     * 优惠卷信息，积分
     */
    private Integer integration;

    /**
     * 令牌，用来防重复提交
     */
    private String orderToken;

    /**
     * 获取商品数量
     * @return 商品数量
     */
    public Integer getCount() {
        if (CollectionUtils.isNotEmpty(items)) {
            int count = 0;
            for (OrderItemVO item : items) {
                count += item.getCount();
            }
            return count;
        }
        return 0;
    }

    /**
     * 获取商品总价
     */
    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(items)) {
            for (OrderItemVO item : items) {
                amount = amount.add(item.getTotalPrice());
            }
        }
        return amount;
    }

    /**
     * 获取应付价格
     */
    public BigDecimal getPayPrice() {
        return getTotalAmount();
    }
}
