package com.hlju.onlineshop.goods.web;

import com.hlju.onlineshop.goods.entity.CategoryEntity;
import com.hlju.onlineshop.goods.service.CategoryService;
import com.hlju.onlineshop.goods.vo.Catalog2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/10 15:46
 */
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    /**
     * 跳转到首页
     * @param model springMVC的Model
     * @return 地址
     */
    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        List<CategoryEntity> categoryEntityList = categoryService.getLevel1Categories(1);
        model.addAttribute("categories", categoryEntityList);
        return "index";
    }

    @ResponseBody
    @RequestMapping("/index/catalog.json")
    public Map<String, List<Catalog2VO>> getCatalogJson() {

        return categoryService.getCatalogJson();
    }

}
