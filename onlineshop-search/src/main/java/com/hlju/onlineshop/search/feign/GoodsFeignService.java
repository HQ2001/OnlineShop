package com.hlju.onlineshop.search.feign;

import com.hlju.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/16 9:52
 */
@FeignClient("onlineshop-goods")
public interface GoodsFeignService {
    @GetMapping("/goods/brand/list-by-ids")
    R brandsByBrandIds(@RequestParam("brandIds") List<Long> brandIds);

    @GetMapping("/goods/category/list-by-ids")
    R categoriesByCategoryIds(@RequestParam("categoryIds") List<Long> categoryIds);
}
