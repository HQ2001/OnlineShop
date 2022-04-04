package com.hlju.onlineshop.goods.feign;

import com.hlju.common.to.SkuReductionTO;
import com.hlju.common.to.SpuBoundsTO;
import com.hlju.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/1 14:40
 */
@FeignClient("onlineshop-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spu-bounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTO spuBoundsTo);

    @PostMapping("/coupon/sku-full-reduction/save/info")
    R saveSkuReduction(@RequestBody SkuReductionTO skuReductionTo);
}
