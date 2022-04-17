package com.hlju.onlineshop.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hlju.onlineshop.search.constant.EsConstant;
import lombok.Data;

import java.util.List;

/**
 * 封装页面传递过来的查询条件
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/14 10:38
 */
@Data
public class SearchParamDTO {

    /**
     * 全文匹配关键字
     */
    private String keyword;

    /**
     * 三级分类id
     */
    private List<Long> categoryIds;

    /**
     * 排序条件
     *  - sort=saleCount_asc/desc
     *  - sort=hotScore_asc/desc
     *  - sort=skuPrice_asc/desc
     */
    private String sort;

    /**
     * 是否有库存(0 - 无    1 - 有)
     */
    private Boolean hasStock;

    /**
     * 最小价格
     */
    private Integer minSkuPrice;

    /**
     * 最大价格
     */
    private Integer maxSkuPrice;

    /**
     * 品牌Id
     */
    private List<Long> brandIds;
    /**
     * 属性
     *  - attrs=[属性id]_[属性值]:[属性值]:...
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNumber = 1;

    /**
     * 页面size
     */
    private Integer pageSize = EsConstant.GOODS_PAGE_SIZE;

    /**
     * 原生的所有查询条件
     */
    private String queryString;

}
