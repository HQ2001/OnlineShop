package com.hlju.onlineshop.user.feign;

import com.hlju.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient("onlineshop-coupon")
public interface CouponFeignService {
    @GetMapping("/coupon/coupon/user/list")
    public R userCoupons();
}
