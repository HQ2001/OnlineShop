package com.hlju.onlineshop.warehouse.service.impl;

import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;
import com.hlju.onlineshop.warehouse.dao.PurchaseDetailDao;
import com.hlju.onlineshop.warehouse.entity.PurchaseDetailEntity;
import com.hlju.onlineshop.warehouse.service.PurchaseDetailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<PurchaseDetailEntity>();

        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(w -> w.eq("purchase_id", key).or().eq("sku_id", key));
        }

        String status = (String) params.get("status");
        if (StringUtils.isNotEmpty(status)) {
            queryWrapper.eq("status", status);
        }

        String warehouseId = (String) params.get("warehouseId");
        if (!StringUtils.isEmpty(warehouseId)) {
            queryWrapper.eq("warehouse_id", warehouseId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}