package com.hlju.onlineshop.warehouse.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hlju.onlineshop.warehouse.entity.WarehouseInfoEntity;
import com.hlju.onlineshop.warehouse.service.WarehouseInfoService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 仓库信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
@RestController
@RequestMapping("/warehouse/warehouseinfo")
public class WarehouseInfoController {
    @Autowired
    private WarehouseInfoService warehouseInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = warehouseInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WarehouseInfoEntity warehouseInfo = warehouseInfoService.getById(id);

        return R.ok().put("warehouseInfo", warehouseInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WarehouseInfoEntity warehouseInfo) {
        warehouseInfoService.save(warehouseInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WarehouseInfoEntity warehouseInfo) {
        warehouseInfoService.updateById(warehouseInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        warehouseInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
