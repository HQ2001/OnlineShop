package com.hlju.onlineshop.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.hlju.common.to.es.SkuEsModel;
import com.hlju.common.utils.R;
import com.hlju.onlineshop.search.conf.ElasticSearchConfig;
import com.hlju.onlineshop.search.constant.EsConstant;
import com.hlju.onlineshop.search.dto.SearchParamDTO;
import com.hlju.onlineshop.search.feign.GoodsFeignService;
import com.hlju.onlineshop.search.service.OnlineShopSearchService;
import com.hlju.onlineshop.search.vo.BrandVO;
import com.hlju.onlineshop.search.vo.CategoryVO;
import com.hlju.onlineshop.search.vo.SearchResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/14 10:41
 */
@Slf4j
@Service
public class OnlineShopSearchServiceImpl implements OnlineShopSearchService {

    private final RestHighLevelClient esClient;
    private final GoodsFeignService goodsFeignService;
    private static final String BRAND_AGG = "brandAgg";
    private static final String BRAND_NAME_AGG = "brandNameAgg";
    private static final String BRAND_IMG_AGG = "brandImgAgg";
    private static final String CATEGORY_AGG = "categoryAgg";
    private static final String CATEGORY_NAME_AGG = "categoryNameAgg";
    private static final String ATTR_AGG = "attrAgg";
    private static final String ATTR_ID_AGG = "attrIdAgg";
    private static final String ATTR_VALUE_AGG = "attrValueAgg";
    private static final String ATTR_NAME_AGG = "attrNameAgg";
    private static final String SKU_TITLE = "skuTitle";

    @Autowired
    OnlineShopSearchServiceImpl(RestHighLevelClient esClient,
                                GoodsFeignService goodsFeignService) {
        this.esClient = esClient;
        this.goodsFeignService = goodsFeignService;
    }

    @Override
    public SearchResponseVO search(SearchParamDTO param) {
        SearchRequest searchRequest = this.buildSearchRequest(param);
        SearchResponseVO responseVO = null;
        try {
            // 发送请求
            SearchResponse response = esClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);

            // 将响应数据进行封装
            responseVO = this.buildSearchResponseVO(response, param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseVO;
    }

    private SearchResponseVO buildSearchResponseVO(SearchResponse response, SearchParamDTO param) {
        SearchResponseVO responseVO = new SearchResponseVO();
        SearchHits hits = response.getHits();
        // 查询到的商品
        List<SkuEsModel> goods = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            String sourceString = hit.getSourceAsString();
            SkuEsModel skuEsModel = JSON.parseObject(sourceString, SkuEsModel.class);
            // 有keyword高亮展示
            if (StringUtils.isNotEmpty(param.getKeyword())) {
                HighlightField highlightField = hit.getHighlightFields().get(SKU_TITLE);
                String skuTitle = highlightField.getFragments()[0].string();
                skuEsModel.setSkuTitle(skuTitle);
            }
            goods.add(skuEsModel);
        }
        responseVO.setGoods(goods);

        Aggregations aggregations = response.getAggregations();

        // 属性信息
        List<SearchResponseVO.AttrVO> attrs = new ArrayList<>();
        ParsedNested attrAgg = aggregations.get(ATTR_AGG);
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get(ATTR_ID_AGG);
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchResponseVO.AttrVO attrVO = new SearchResponseVO.AttrVO();
            attrVO.setAttrId(bucket.getKeyAsNumber().longValue());
            // 属性名
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get(ATTR_NAME_AGG);
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attrVO.setAttrName(attrName);
            // 属性图片
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get(ATTR_VALUE_AGG);
            List<String> attrValues = attrValueAgg.getBuckets().stream()
                    .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                    .collect(Collectors.toList());
            attrVO.setAttrValues(attrValues);
            attrs.add(attrVO);
        }
        responseVO.setAttrs(attrs);
        // 品牌信息
        List<SearchResponseVO.BrandVO> brands = new ArrayList<>();
        ParsedLongTerms brandAgg = aggregations.get(BRAND_AGG);
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            SearchResponseVO.BrandVO brandVO = new SearchResponseVO.BrandVO();
            brandVO.setBrandId(bucket.getKeyAsNumber().longValue());
            // 品牌名
            ParsedStringTerms brandNameAgg = bucket.getAggregations().get(BRAND_NAME_AGG);
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            brandVO.setBrandName(brandName);
            // 品牌图片
            ParsedStringTerms brandImgAgg = bucket.getAggregations().get(BRAND_IMG_AGG);
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            brandVO.setBrandImg(brandImg);
            brands.add(brandVO);
        }
        responseVO.setBrands(brands);
        // 分类信息
        List<SearchResponseVO.CategoryVO> categories = new ArrayList<>();
        ParsedLongTerms categoryAgg = aggregations.get(CATEGORY_AGG);
        for (Terms.Bucket bucket : categoryAgg.getBuckets()) {
            SearchResponseVO.CategoryVO categoryVO = new SearchResponseVO.CategoryVO();
            categoryVO.setCategoryId(bucket.getKeyAsNumber().longValue());
            ParsedStringTerms categoryNameAgg = bucket.getAggregations().get(CATEGORY_NAME_AGG);
            String categoryName = categoryNameAgg.getBuckets().get(0).getKeyAsString();
            categoryVO.setCategoryName(categoryName);
            categories.add(categoryVO);
        }
        responseVO.setCategories(categories);

        // 分页信息
        long total = hits.getTotalHits().value;
        responseVO.setTotal(total);
        int maxPageNumber = this.getPageNumber(total, EsConstant.GOODS_PAGE_SIZE);
        responseVO.setMaxPageNumber(maxPageNumber);
        responseVO.setPageNumber(param.getPageNumber());
        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= maxPageNumber; i++) {
            pageNavs.add(i);
        }
        responseVO.setPageNavs(pageNavs);

        // 被筛选了的属性ids
        List<Long> attrIds = new ArrayList<>();
        responseVO.setAttrIds(attrIds);
        // 面包屑导航
        Map<Long, SearchResponseVO.AttrVO> attrVOMap = attrs.stream()
                .collect(Collectors.toMap(SearchResponseVO.AttrVO::getAttrId, item -> item));
        List<SearchResponseVO.NavVO> navs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(param.getAttrs())) {
            for (String attrStr : param.getAttrs()) {
                SearchResponseVO.NavVO vo = new SearchResponseVO.NavVO();
                String[] s = attrStr.split("_");
                long attrId = Long.parseLong(s[0]);
                SearchResponseVO.AttrVO attrVO = attrVOMap.get(attrId);
                attrIds.add(attrId);
                if (Objects.nonNull(attrVO)) {
                    vo.setNavName(attrVO.getAttrName());

                } else {
                    log.error("属性不存在, ID: {}", s[0]);
                }
                vo.setNavValues(Arrays.asList(s[1].split(":")));
                String linkParam = replaceQueryString(param.getQueryString(), "attrs", attrStr);
                vo.setCancelLinkParam(linkParam);
                navs.add(vo);
            }
        }
        // 品牌、分类的面包屑导航
        if (CollectionUtils.isNotEmpty(param.getBrandIds())) {
            SearchResponseVO.NavVO vo = new SearchResponseVO.NavVO();
            vo.setNavName("品牌");
            R r = goodsFeignService.brandsByBrandIds(param.getBrandIds());
            if (r.getCode() == 0) {
                List<BrandVO> brandVOS = r.getData("brands", new TypeReference<List<BrandVO>>() {
                });
                List<String> brandNames = new ArrayList<>();
                String linkParam = param.getQueryString();
                for (BrandVO brandVO : brandVOS) {
                    brandNames.add(brandVO.getName());
                    linkParam = replaceQueryString(linkParam, "brandIds", brandVO.getBrandId().toString());
                }
                vo.setNavValues(brandNames);
                vo.setCancelLinkParam(linkParam);
            }
            navs.add(vo);
        }
        if (CollectionUtils.isNotEmpty(param.getCategoryIds())) {
            SearchResponseVO.NavVO vo = new SearchResponseVO.NavVO();
            vo.setNavName("分类");
            R r = goodsFeignService.categoriesByCategoryIds(param.getCategoryIds());
            if (r.getCode() == 0) {
                List<CategoryVO> categoryVOS = r.getData("categories", new TypeReference<List<CategoryVO>>() {
                });
                List<String> brandNames = new ArrayList<>();
                String linkParam = param.getQueryString();
                for (CategoryVO categoryVO : categoryVOS) {
                    brandNames.add(categoryVO.getName());
                    linkParam = replaceQueryString(linkParam, "categoryIds", categoryVO.getCatId().toString());
                }
                vo.setNavValues(brandNames);
                vo.setCancelLinkParam(linkParam);
            }
            navs.add(vo);
        }
        responseVO.setNavs(navs);

        return responseVO;
    }

    /**
     * 删除查询中相应的条件
     *
     * @param queryString 查询参数
     * @param key   要删除的key
     * @param value 要删除的value
     * @return 删除后的字符串
     */
    private String replaceQueryString(String queryString, String key, String value) {
        try {
            String encodeAttrStr = URLEncoder.encode(value, "UTF-8");
            // 浏览器默认转成%20，而java转成+
            encodeAttrStr = encodeAttrStr.replace("+", "%20");
            String link = queryString.replace("&" + key + "=" + encodeAttrStr, "")
                    .replace(key + "=" + encodeAttrStr, "");
            link = (StringUtils.isEmpty(link) ? "" : "?") + link;
            return link;
        } catch (UnsupportedEncodingException e) {
            log.error("encode error ", e);
        }
        return "";
    }

    /**
     * 获取页码
     *
     * @param recordsNumber 记录数
     * @param pageSize      页size
     * @return 页码
     */
    private int getPageNumber(long recordsNumber, int pageSize) {
        return recordsNumber % pageSize == 0L ? (int) (recordsNumber / pageSize) : (int) (recordsNumber / pageSize) + 1;
    }

    private SearchRequest buildSearchRequest(SearchParamDTO param) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        searchSourceBuilder.query(boolQuery);

        // 构建must部分
        if (StringUtils.isNotEmpty(param.getKeyword())) {
            MatchQueryBuilder skuTitleQuery = QueryBuilders.matchQuery(SKU_TITLE, param.getKeyword());
            boolQuery.must(skuTitleQuery);
        }
        // 构建filter部分
        if (CollectionUtils.isNotEmpty(param.getCategoryIds())) {
            TermsQueryBuilder categoryIdQuery = QueryBuilders.termsQuery("categoryId", param.getCategoryIds());
            boolQuery.filter(categoryIdQuery);
        }
        if (CollectionUtils.isNotEmpty(param.getBrandIds())) {
            TermsQueryBuilder brandIdQuery = QueryBuilders.termsQuery("brandId", param.getBrandIds());
            boolQuery.filter(brandIdQuery);
        }
        // attrs部分
        if (CollectionUtils.isNotEmpty(param.getAttrs())) {
            for (String attr : param.getAttrs()) {
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                String[] s = attr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                NestedQueryBuilder attrsQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(attrsQuery);
            }
        }
        if (Objects.nonNull(param.getHasStock())) {
            TermQueryBuilder hasStockQuery = QueryBuilders.termQuery("hasStock", param.getHasStock());
            boolQuery.filter(hasStockQuery);
        }
        if (Objects.nonNull(param.getMinSkuPrice()) || Objects.nonNull(param.getMaxSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            boolQuery.filter(rangeQuery);
            if (Objects.nonNull(param.getMinSkuPrice())) {
                rangeQuery.from(param.getMinSkuPrice());
            }
            if (Objects.nonNull(param.getMaxSkuPrice())) {
                rangeQuery.to(param.getMaxSkuPrice());
            }
        }

        // 排序
        if (StringUtils.isNotEmpty(param.getSort())) {
            String[] s = param.getSort().split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            searchSourceBuilder.sort(s[0], order);
        }

        // 分页
        searchSourceBuilder.from((param.getPageNumber() - 1) * param.getPageSize());
        searchSourceBuilder.size(param.getPageSize());

        // 高亮
        if (StringUtils.isNotEmpty(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field(SKU_TITLE);
            highlightBuilder.preTags("<b style='color: red';>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        // 聚合部分
        // 品牌
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms(BRAND_AGG).field("brandId");
        TermsAggregationBuilder brandNameAgg = AggregationBuilders.terms(BRAND_NAME_AGG).field("brandName");
        brandAgg.subAggregation(brandNameAgg);
        TermsAggregationBuilder brandImgAgg = AggregationBuilders.terms(BRAND_IMG_AGG).field("brandImg");
        brandAgg.subAggregation(brandImgAgg);
        searchSourceBuilder.aggregation(brandAgg);
        // 分类
        TermsAggregationBuilder categoryAgg = AggregationBuilders.terms(CATEGORY_AGG).field("categoryId");
        TermsAggregationBuilder categoryNameAgg = AggregationBuilders.terms(CATEGORY_NAME_AGG).field("categoryName");
        categoryAgg.subAggregation(categoryNameAgg);
        searchSourceBuilder.aggregation(categoryAgg);
        // 属性
        NestedAggregationBuilder nestedAgg = AggregationBuilders.nested(ATTR_AGG, "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms(ATTR_ID_AGG).field("attrs.attrId");
        nestedAgg.subAggregation(attrIdAgg);
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms(ATTR_VALUE_AGG).field("attrs.attrValue");
        attrIdAgg.subAggregation(attrValueAgg);
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms(ATTR_NAME_AGG).field("attrs.attrName");
        attrIdAgg.subAggregation(attrNameAgg);
        searchSourceBuilder.aggregation(nestedAgg);

        log.info("DSL: {}", searchSourceBuilder);

        return new SearchRequest(
                new String[]{EsConstant.GOODS_INDEX},
                searchSourceBuilder
        );
    }
}
