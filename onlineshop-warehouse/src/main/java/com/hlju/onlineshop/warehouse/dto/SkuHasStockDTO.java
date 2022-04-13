package com.hlju.onlineshop.warehouse.dto;

import lombok.Data;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/9 21:01
 */
@Data
public class SkuHasStockDTO {

    private Long skuId;
    private Boolean hasStock;

}
