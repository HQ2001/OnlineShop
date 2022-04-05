package com.hlju.onlineshop.goods.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/3/29 8:57
 */
@Data
public class UserPriceDTO {
    private Long id;
    private String name;
    private BigDecimal price;
}
