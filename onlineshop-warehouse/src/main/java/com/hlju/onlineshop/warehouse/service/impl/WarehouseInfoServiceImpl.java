package com.hlju.onlineshop.warehouse.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.warehouse.dao.WarehouseInfoDao;
import com.hlju.onlineshop.warehouse.entity.WarehouseInfoEntity;
import com.hlju.onlineshop.warehouse.service.WarehouseInfoService;


@Service("warehouseInfoService")
public class WarehouseInfoServiceImpl extends ServiceImpl<WarehouseInfoDao, WarehouseInfoEntity> implements WarehouseInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WarehouseInfoEntity> page = this.page(
                new Query<WarehouseInfoEntity>().getPage(params),
                new QueryWrapper<WarehouseInfoEntity>()
        );

        return new PageUtils(page);
    }

}