package com.hlju.onlineshop.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.warehouse.entity.WarehouseOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
public interface WarehouseOrderTaskDetailService extends IService<WarehouseOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

