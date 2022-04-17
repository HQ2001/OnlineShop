package com.hlju.onlineshop.search.vo;

import com.hlju.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/14 11:06
 */
@Data
public class SearchResponseVO {
    /**
     * 商品信息
     */
    private List<SkuEsModel> goods;

    /**
     * 返回的可选品牌
     */
    private List<BrandVO> brands;

    /**
     * 返回的可选属性
     */
    private List<AttrVO> attrs;

    /**
     * 返回的可选分类
     */
    private List<CategoryVO> categories;

    /**
     * 面包屑导航数据
     */
    private List<NavVO> navs;

    @Data
    public static class NavVO {
        private String navName;
        private List<String> navValues;
        // 取消后跳转到哪
        private String cancelLinkParam;
    }

    /**
     * 被筛选了的属性ids
     */
    private List<Long> attrIds;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 可选页码
     */
    private List<Integer> pageNavs;

    /**
     * 最大页码
     */
    private Integer maxPageNumber;

    /**
     * 总数
     */
    private Long total;

    @Data
    public static class BrandVO {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class AttrVO {
        private Long attrId;
        private String attrName;
        private List<String> attrValues;
    }

    @Data
    public static class CategoryVO {
        private Long categoryId;
        private String categoryName;
    }
}
