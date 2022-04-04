package com.hlju.onlineshop.goods.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.goods.entity.SpuImagesEntity;
import com.hlju.onlineshop.goods.service.SpuImagesService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * spu图片
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/spuimages")
public class SpuImagesController {
    @Autowired
    private SpuImagesService spuImagesService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuImagesService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuImagesEntity spuImages = spuImagesService.getById(id);

        return R.ok().put("spuImages", spuImages);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SpuImagesEntity spuImages) {
        spuImagesService.save(spuImages);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody SpuImagesEntity spuImages) {
        spuImagesService.updateById(spuImages);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuImagesService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
