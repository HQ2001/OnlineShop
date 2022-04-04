package com.hlju.onlineshop.warehouse.service.impl;

import com.google.common.collect.Lists;
import com.hlju.onlineshop.warehouse.feign.GoodsFeignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.warehouse.dao.WarehouseSkuDao;
import com.hlju.onlineshop.warehouse.entity.WarehouseSkuEntity;
import com.hlju.onlineshop.warehouse.service.WarehouseSkuService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("warehouseSkuService")
public class WarehouseSkuServiceImpl extends ServiceImpl<WarehouseSkuDao, WarehouseSkuEntity> implements WarehouseSkuService {

    private final GoodsFeignService goodsFeignService;

    @Autowired
    WarehouseSkuServiceImpl(GoodsFeignService goodsFeignService) {
        this.goodsFeignService = goodsFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WarehouseSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (StringUtils.isNotEmpty(skuId) && Long.parseLong(skuId) > 0L) {
            queryWrapper.eq("sku_id", skuId);
        }
        String warehouseId = (String) params.get("warehouseId");
        if (StringUtils.isNotEmpty(warehouseId) && Long.parseLong(warehouseId) > 0L) {
            queryWrapper.eq("warehouse_id", warehouseId);
        }
        IPage<WarehouseSkuEntity> page = this.page(
                new Query<WarehouseSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void addStocks(List<WarehouseSkuEntity> warehouseSkuEntities) {
        // TODO 需要判断同一个仓库同一个商品有两个实体，要进行合并
        List<WarehouseSkuEntity> exists = baseMapper.listBySkuIdsAndWarehouseIds(warehouseSkuEntities);
        List<WarehouseSkuEntity> inserts = Lists.newArrayList();
        List<WarehouseSkuEntity> updates = Lists.newArrayList();
        List<Long> skuIds = Lists.newArrayList();
        for (WarehouseSkuEntity warehouseSkuEntity : warehouseSkuEntities) {
            boolean isExist = false;
            for (WarehouseSkuEntity exist : exists) {
                if (Objects.equals(warehouseSkuEntity.getSkuId(), exist.getSkuId())
                        && Objects.equals(warehouseSkuEntity.getWarehouseId(), exist.getWarehouseId())) {
                    isExist = true;
                    break;
                }
            }
            if (isExist) {
                updates.add(warehouseSkuEntity);
            } else {
                inserts.add(warehouseSkuEntity);
            }
            skuIds.add(warehouseSkuEntity.getSkuId());
        }
        baseMapper.updateAddStocks(updates);
        try {
            List<Map<String, Object>> skus = (List<Map<String, Object>>) goodsFeignService.listBySkuIds(skuIds).get("skus");
            Map<Integer, Map<String, Object>> skuIdAndSkuInfoMap = skus.stream()
                    .collect(Collectors.toMap(item -> (Integer) item.get("skuId"), item -> item));
            inserts.forEach(item ->
                    item.setSkuName((String) skuIdAndSkuInfoMap.get(item.getSkuId().intValue()).get("skuName"))
            );
        } catch (Exception e) {
            log.error("sku名字设置失败 ", e);
        }
        this.saveBatch(inserts);
    }

}