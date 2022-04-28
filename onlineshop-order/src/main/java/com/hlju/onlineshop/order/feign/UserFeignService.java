package com.hlju.onlineshop.order.feign;

import com.hlju.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/26 9:59
 */
@FeignClient("onlineshop-user")
public interface UserFeignService {

    @GetMapping("/user/user-receive-address/addresses/{userId}")
    R getAddressesByUserId(@PathVariable("userId") Long userId);

}
