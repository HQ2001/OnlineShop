package com.hlju.onlineshop.goods.feign;

import com.hlju.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/10 8:43
 */
@FeignClient("onlineshop-warehouse")
public interface WarehouseFeignService {

    @PostMapping("/warehouse/warehouse-sku/has-stock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);

}
