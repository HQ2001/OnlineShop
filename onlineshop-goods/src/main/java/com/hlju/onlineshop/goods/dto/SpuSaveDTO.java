package com.hlju.onlineshop.goods.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/3/29 8:48
 */
@Data
public class SpuSaveDTO {
    /**
     * 商品名
     */
    private String spuName;
    /**
     * 商品描述
     */
    private String spuDescription;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 上架状态[0 - 下架，1 - 上架]
     */
    private Integer publishStatus;
    /**
     * 描述图片
     */
    private List<String> description;
    /**
     * 图片集
     */
    private List<String> images;
    /**
     * 积分信息
     */
    private BoundsDTO bounds;
    /**
     * 基础属性
     */
    private List<BaseAttrDTO> baseAttrs;
    /**
     * 对应的所有sku
     */
    private List<SkuDTO> skus;
}
