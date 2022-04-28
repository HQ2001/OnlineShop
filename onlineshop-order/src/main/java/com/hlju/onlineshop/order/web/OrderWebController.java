package com.hlju.onlineshop.order.web;

import com.hlju.onlineshop.order.service.OrderService;
import com.hlju.onlineshop.order.vo.OrderConfirmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/25 21:27
 */
@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model) throws InterruptedException {

        OrderConfirmVO vo = orderService.getConfirmOrder();

        model.addAttribute("orderConfirmData", vo);

        return "confirm";
    }

}
