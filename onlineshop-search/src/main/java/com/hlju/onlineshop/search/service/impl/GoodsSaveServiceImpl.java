package com.hlju.onlineshop.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.hlju.common.to.es.SkuEsModel;
import com.hlju.onlineshop.search.EsConstant;
import com.hlju.onlineshop.search.conf.ElasticSearchConfig;
import com.hlju.onlineshop.search.service.GoodsSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/10 9:22
 */
@Slf4j
@Service("goodsSaveService")
public class GoodsSaveServiceImpl implements GoodsSaveService {

    private final RestHighLevelClient restHighLevelClient;

    GoodsSaveServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public boolean goodsUp(List<SkuEsModel> skuEsModels) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.GOODS_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
        return !bulk.hasFailures();
    }
}
