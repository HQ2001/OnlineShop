package com.hlju.onlineshop.warehouse.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hlju.onlineshop.warehouse.entity.WarehouseOrderTaskEntity;
import com.hlju.onlineshop.warehouse.service.WarehouseOrderTaskService;
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
@RequestMapping("/warehouse/warehouseordertask")
public class WarehouseOrderTaskController {
    @Autowired
    private WarehouseOrderTaskService warehouseOrderTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = warehouseOrderTaskService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
            WarehouseOrderTaskEntity warehouseOrderTask = warehouseOrderTaskService.getById(id);

        return R.ok().put("warehouseOrderTask", warehouseOrderTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WarehouseOrderTaskEntity warehouseOrderTask) {
            warehouseOrderTaskService.save(warehouseOrderTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WarehouseOrderTaskEntity warehouseOrderTask) {
            warehouseOrderTaskService.updateById(warehouseOrderTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
            warehouseOrderTaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
