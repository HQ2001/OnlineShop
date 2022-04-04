package com.hlju.onlineshop.goods.controller;

import java.util.Arrays;
import java.util.Map;

import com.hlju.onlineshop.goods.dto.SpuSaveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.goods.entity.SpuInfoEntity;
import com.hlju.onlineshop.goods.service.SpuInfoService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * spu信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/spu-info")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SpuSaveDTO spuSaveDto) {
        // spuInfoService.save(spuInfo);
        spuInfoService.saveSpuDetail(spuSaveDto);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
