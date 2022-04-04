package com.hlju.onlineshop.warehouse.feign;

import com.hlju.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/3 20:01
 */
@FeignClient("onlineshop-goods")
public interface GoodsFeignService {

    @GetMapping("/goods/sku-info/list-by-sku-ids")
    R listBySkuIds(@RequestParam("skuIds") List<Long> skuIds);

}
