package com.hlju.onlineshop.goods.web;

import com.hlju.onlineshop.goods.service.SkuInfoService;
import com.hlju.onlineshop.goods.vo.SkuInfoDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品详情controller
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/17 21:14
 */
@Controller
public class GoodsInfoController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuInfo(@PathVariable("skuId") Long skuId, Model model) {
        SkuInfoDetailVO vo = skuInfoService.infoDetail(skuId);
        model.addAttribute("info", vo);
        return "infoDetail";
    }

}
