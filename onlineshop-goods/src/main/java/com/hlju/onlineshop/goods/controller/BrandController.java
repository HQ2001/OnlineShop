package com.hlju.onlineshop.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hlju.common.valid.AddGroup;
import com.hlju.common.valid.UpdateGroup;
import com.hlju.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.goods.entity.BrandEntity;
import com.hlju.onlineshop.goods.service.BrandService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 品牌
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    @GetMapping("/list-by-ids")
    public R brandsByBrandIds(@RequestParam("brandIds") List<Long> brandIds) {
        List<BrandEntity> brands = brandService.listByIds(brandIds);

        return R.ok().put("brands", brands);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody @Validated({AddGroup.class}) BrandEntity brand) {
        brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody @Validated({UpdateGroup.class}) BrandEntity brand) {
        brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 修改品牌状态
     */
    @RequestMapping("/update-status")
    public R updateStatus(@RequestBody @Validated({UpdateStatusGroup.class}) BrandEntity brand) {
        BrandEntity newBrand = new BrandEntity();
        newBrand.setBrandId(brand.getBrandId());
        newBrand.setShowStatus(brand.getShowStatus());
        brandService.updateById(newBrand);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
