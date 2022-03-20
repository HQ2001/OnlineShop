package com.hlju.onlineshop.warehouse.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.warehouse.dao.WarehouseOrderTaskDao;
import com.hlju.onlineshop.warehouse.entity.WarehouseOrderTaskEntity;
import com.hlju.onlineshop.warehouse.service.WarehouseOrderTaskService;


@Service("warehouseOrderTaskService")
public class WarehouseOrderTaskServiceImpl extends ServiceImpl<WarehouseOrderTaskDao, WarehouseOrderTaskEntity> implements WarehouseOrderTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WarehouseOrderTaskEntity> page = this.page(
                new Query<WarehouseOrderTaskEntity>().getPage(params),
                new QueryWrapper<WarehouseOrderTaskEntity>()
        );

        return new PageUtils(page);
    }

}