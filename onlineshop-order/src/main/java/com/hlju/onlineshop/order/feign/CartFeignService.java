package com.hlju.onlineshop.order.feign;

import com.hlju.onlineshop.order.vo.OrderItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/26 11:20
 */
@FeignClient("onlineshop-cart")
public interface CartFeignService {

    /**
     * 获取最新的已选中的购物项
     * @return
     */
    @ResponseBody
    @GetMapping("/cartItems")
    List<OrderItemVO> getUpToDateCheckedCartItems(@RequestParam("userId") Long userId);

}
