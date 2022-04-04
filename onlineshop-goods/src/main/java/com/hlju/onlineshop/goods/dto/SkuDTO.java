package com.hlju.onlineshop.goods.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/3/29 8:55
 */
@Data
public class SkuDTO {

    private List<SkuAttrDTO> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<ImageDTO> images;
    private List<String> descar;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer priceStatus;
    private List<UserPriceDTO> userPrice;

}
