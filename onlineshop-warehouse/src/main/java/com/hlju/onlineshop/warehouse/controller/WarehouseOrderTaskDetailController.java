package com.hlju.onlineshop.warehouse.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.warehouse.entity.WarehouseOrderTaskDetailEntity;
import com.hlju.onlineshop.warehouse.service.WarehouseOrderTaskDetailService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 库存工作单
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
@RestController
@RequestMapping("/warehouse/warehouse-order-task-detail")
public class WarehouseOrderTaskDetailController {
    @Autowired
    private WarehouseOrderTaskDetailService warehouseOrderTaskDetailService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = warehouseOrderTaskDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WarehouseOrderTaskDetailEntity warehouseOrderTaskDetail = warehouseOrderTaskDetailService.getById(id);

        return R.ok().put("warehouseOrderTaskDetail", warehouseOrderTaskDetail);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody WarehouseOrderTaskDetailEntity warehouseOrderTaskDetail) {
        warehouseOrderTaskDetailService.save(warehouseOrderTaskDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody WarehouseOrderTaskDetailEntity warehouseOrderTaskDetail) {
        warehouseOrderTaskDetailService.updateById(warehouseOrderTaskDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        warehouseOrderTaskDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
