package com.hlju.onlineshop.goods.vo;

import com.hlju.onlineshop.goods.entity.SkuImagesEntity;
import com.hlju.onlineshop.goods.entity.SkuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/17 21:38
 */
@Data
public class SkuInfoDetailVO {

    /**
     * sku基本信息
     */
    private SkuInfoEntity skuInfoEntity;

    /**
     * sku图片信息 gms_sku_images
     */
    private List<SkuImagesEntity> skuImages;

    /**
     * spu销售属性组合(其他sku)
     */
    private List<SaleAttrVO> saleAttrs;

    /**
     * spu的规格参数信息(属性组-属性)
     */
    List<AttrGroupVO> attrGroups;

    @Data
    public static class AttrGroupVO {
        private String groupName;
        List<BaseAttrVO> baseAttrs;
    }

    @Data
    public static class BaseAttrVO {
        private String attrName;
        private String attrValue;
    }

    @Data
    public static class SaleAttrVO {
        private Long attrId;
        private String attrName;
        private List<String> attrValues;
    }
}
