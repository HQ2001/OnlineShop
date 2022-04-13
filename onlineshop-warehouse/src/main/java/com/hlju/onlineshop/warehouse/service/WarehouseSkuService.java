package com.hlju.onlineshop.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.warehouse.entity.WarehouseSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
public interface WarehouseSkuService extends IService<WarehouseSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 增加库存
     * @param warehouseSkuEntities 实体list
     */
    void addStocks(List<WarehouseSkuEntity> warehouseSkuEntities);

    /**
     * 根据skuId查询出相应商品是否有库存
     * @param skuIds skuIds
     * @return k - skuId   v - 有没有库存
     */
    Map<Long, Boolean> getSkusHasStock(List<Long> skuIds);
}

