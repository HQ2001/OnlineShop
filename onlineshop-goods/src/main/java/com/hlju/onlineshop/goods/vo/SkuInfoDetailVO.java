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
     * spu介绍（图片）
     */
    private String spuDescriptionImages;

    /**
     * spu销售属性组合(其他sku)
     */
    private List<SaleAttrVO> saleAttrs;

    /**
     * spu的规格参数信息(属性组-属性)
     */
    List<AttrGroupVO> attrGroups;

    /**
     * 是否有货
     */
    private Boolean hasStock = true;

    @Data
    public static class AttrGroupVO {
        private String groupName;
        List<BaseAttrVO> baseAttrs;
    }

    @Data
    public static class BaseAttrVO {
        private String attrName;
        private String attrValues;
    }

    @Data
    public static class SaleAttrVO {
        private String attrName;
        private List<AttrValueWithSkuIdVO> attrValues;
    }

    @Data
    public static class AttrValueWithSkuIdVO {
        private String attrValue;
        // 用于确定不同销售属性值进行组合后的skuId
        // 两个销售属性值组合时候，重合的那个就是组合成的skuId
        private List<Long> skuIds;
    }
}
