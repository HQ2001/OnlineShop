package com.hlju.onlineshop.goods.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.goods.entity.SkuInfoEntity;
import com.hlju.onlineshop.goods.service.SkuInfoService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * sku信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/sku-info")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 获取最新的商品价格
     *
     * @param skuIds skuIds
     * @return k - skuId   v - 最新的价格
     */
    @GetMapping("/getUpToDatePrice")
    public Map<Long, BigDecimal> getUpToDatePrice(@RequestParam("skuIds") List<Long> skuIds) {
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.listByIds(skuIds);
        return skuInfoEntities.stream()
                .collect(Collectors.toMap(
                        SkuInfoEntity::getSkuId,
                        SkuInfoEntity::getPrice
                ));
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/list-by-sku-ids")
    public R listBySkuIds(@RequestParam("skuIds") List<Long> skuIds) {
        List<SkuInfoEntity> skus = skuInfoService.listByIds(skuIds);

        return R.ok().put("skus", skus);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId) {
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] skuIds) {
        skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
