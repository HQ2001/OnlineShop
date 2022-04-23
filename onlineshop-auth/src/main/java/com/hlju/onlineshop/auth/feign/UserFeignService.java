package com.hlju.onlineshop.auth.feign;

import com.hlju.common.utils.R;
import com.hlju.onlineshop.auth.dto.UserLoginDTO;
import com.hlju.onlineshop.auth.dto.UserRegisterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/22 18:15
 */
@FeignClient("onlineshop-user")
public interface UserFeignService {

    @PostMapping("/user/user/register")
    R register(@RequestBody UserRegisterDTO dto);

    @PostMapping("/user/user/login")
    R login(@RequestBody UserLoginDTO dto);
}
