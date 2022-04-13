package com.hlju.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品上架时保存到es中的json格式
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/9 10:39
 */
@Data
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long categoryId;
    private String brandName;
    private String brandImg;
    private String categoryName;

    private List<Attr> attrs;

    @Data
    public static class Attr {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

}
