package com.hlju.onlineshop.warehouse.dao;

import com.hlju.onlineshop.warehouse.entity.WarehouseSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
@Mapper
public interface WarehouseSkuDao extends BaseMapper<WarehouseSkuEntity> {

    /**
     * 根据列表中的skuId和仓库id获取实体
     * @param warehouseSkuEntities 包含skuId和warehouseId的实体
     * @return 存在的实体
     */
    List<WarehouseSkuEntity> listBySkuIdsAndWarehouseIds(@Param("list") List<WarehouseSkuEntity> warehouseSkuEntities);

    /**
     * 更新库存，增加相应库存量
     * @param exists 实体类
     */
    void updateAddStocks(@Param("list") List<WarehouseSkuEntity> exists);
}
