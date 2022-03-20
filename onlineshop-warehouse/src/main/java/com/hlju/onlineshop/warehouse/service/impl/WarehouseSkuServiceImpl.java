package com.hlju.onlineshop.warehouse.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.warehouse.dao.WarehouseSkuDao;
import com.hlju.onlineshop.warehouse.entity.WarehouseSkuEntity;
import com.hlju.onlineshop.warehouse.service.WarehouseSkuService;


@Service("warehouseSkuService")
public class WarehouseSkuServiceImpl extends ServiceImpl<WarehouseSkuDao, WarehouseSkuEntity> implements WarehouseSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WarehouseSkuEntity> page = this.page(
                new Query<WarehouseSkuEntity>().getPage(params),
                new QueryWrapper<WarehouseSkuEntity>()
        );

        return new PageUtils(page);
    }

}