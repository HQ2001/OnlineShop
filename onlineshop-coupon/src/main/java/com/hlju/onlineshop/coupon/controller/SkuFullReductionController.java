package com.hlju.onlineshop.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.hlju.common.to.SkuReductionTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.coupon.entity.SkuFullReductionEntity;
import com.hlju.onlineshop.coupon.service.SkuFullReductionService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 商品满减信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 10:19:10
 */
@RestController
@RequestMapping("/coupon/sku-full-reduction")
public class SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /**
     * 保存sku满减信息
     */
    @PostMapping("/save/info")
    public R saveSkuReduction(@RequestBody SkuReductionTO skuReductionTo) {
        skuFullReductionService.saveSkuReduction(skuReductionTo);
        return R.ok();
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction) {
        skuFullReductionService.save(skuFullReduction);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction) {
        skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
