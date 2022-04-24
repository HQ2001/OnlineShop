package com.hlju.onlineshop.cart.feign;

import com.hlju.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/24 10:54
 */
@FeignClient("onlineshop-goods")
public interface GoodsFeignService {

    @RequestMapping("/goods/sku-info/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

    @GetMapping("/goods/sku-sale-attr-value/sale-attr/string-value")
    R getSkuSaleAttrValues(@RequestParam("skuId") Long skuId);

}
