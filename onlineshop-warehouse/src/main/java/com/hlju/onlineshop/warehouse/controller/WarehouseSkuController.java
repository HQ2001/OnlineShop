package com.hlju.onlineshop.warehouse.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.warehouse.entity.WarehouseSkuEntity;
import com.hlju.onlineshop.warehouse.service.WarehouseSkuService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 商品库存
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
@RestController
@RequestMapping("/warehouse/warehouse-sku")
public class WarehouseSkuController {
    @Autowired
    private WarehouseSkuService warehouseSkuService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = warehouseSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WarehouseSkuEntity warehouseSku = warehouseSkuService.getById(id);

        return R.ok().put("warehouseSku", warehouseSku);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody WarehouseSkuEntity warehouseSku) {
        warehouseSkuService.save(warehouseSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody WarehouseSkuEntity warehouseSku) {
        warehouseSkuService.updateById(warehouseSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        warehouseSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
