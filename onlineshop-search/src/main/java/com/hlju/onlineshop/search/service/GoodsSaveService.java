package com.hlju.onlineshop.search.service;

import com.hlju.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/10 9:20
 */
public interface GoodsSaveService {
    /**
     * 商品上架
     * @param skuEsModels 要存到es的models
     * @return true-正常执行，false-有错误
     */
    boolean goodsUp(List<SkuEsModel> skuEsModels) throws IOException;
}
