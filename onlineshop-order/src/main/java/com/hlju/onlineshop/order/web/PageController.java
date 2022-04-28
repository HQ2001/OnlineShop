package com.hlju.onlineshop.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/25 20:28
 */
@Controller
public class PageController {

    @GetMapping("/{page}")
    public String listPage(@PathVariable("page") String page) {
        return page;
    }

}
