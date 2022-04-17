package com.hlju.onlineshop.search.controller;

import com.hlju.onlineshop.search.dto.SearchParamDTO;
import com.hlju.onlineshop.search.service.OnlineShopSearchService;
import com.hlju.onlineshop.search.vo.SearchResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/14 10:15
 */
@Controller
public class SearchController {

    @Autowired
    OnlineShopSearchService searchService;

    @GetMapping("list.html")
    public String listPage(SearchParamDTO param, Model model, HttpServletRequest request) {
        param.setQueryString(request.getQueryString());
        SearchResponseVO result = searchService.search(param);
        model.addAttribute("result", result);
        model.addAttribute("param", param);
        return "list";
    }

}
