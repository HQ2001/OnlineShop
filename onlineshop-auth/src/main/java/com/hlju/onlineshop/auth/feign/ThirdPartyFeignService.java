package com.hlju.onlineshop.auth.feign;

import com.hlju.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/21 11:17
 */
@FeignClient("onlineshop-third-party")
public interface ThirdPartyFeignService {

    @GetMapping("/sms/sendCode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
