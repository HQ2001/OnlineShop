package com.hlju.onlineshop.search.controller;

import com.hlju.common.exception.BizCodeEnum;
import com.hlju.common.to.es.SkuEsModel;
import com.hlju.common.utils.R;
import com.hlju.onlineshop.search.service.GoodsSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/10 9:16
 */
@Slf4j
@RestController
@RequestMapping("/es/save")
public class EsSaveController {

    @Autowired
    private GoodsSaveService goodsSaveService;

    /**
     * 商品上架
     */
    @PostMapping("/goods")
    public R goodsUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean res;
        try {
            res = goodsSaveService.goodsUp(skuEsModels);
        } catch (Exception e) {
            log.error("商品上架错误", e);
            return R.error(BizCodeEnum.GOODS_UP_EXCEPTION.getCode(), BizCodeEnum.GOODS_UP_EXCEPTION.getMsg());
        }
        return res ? R.ok() : R.error(BizCodeEnum.GOODS_UP_EXCEPTION.getCode(), BizCodeEnum.GOODS_UP_EXCEPTION.getMsg());
    }

}
