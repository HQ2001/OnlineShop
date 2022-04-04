package com.hlju.onlineshop.goods.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.SkuInfoDao;
import com.hlju.onlineshop.goods.entity.SkuInfoEntity;
import com.hlju.onlineshop.goods.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and((wrapper) -> wrapper.eq("sku_id", key).or().like("sku_name", key));
        }

        setQueryCondition(queryWrapper, params);

        String min = (String) params.get("min");
        if (StringUtils.isNotEmpty(min)) {
            queryWrapper.ge("price", min);
        }
        String max = (String) params.get("max");
        if (StringUtils.isNotEmpty(max) && Long.parseLong(max) > 0L) {
            queryWrapper.le("price", max);
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    public void setQueryCondition(QueryWrapper<?> queryWrapper, Map<String, Object> params) {
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId) && Long.parseLong(brandId) > 0L) {
            queryWrapper.eq("brand_id", brandId);
        }
        String categoryId = (String) params.get("categoryId");
        if (StringUtils.isNotEmpty(categoryId) && Long.parseLong(categoryId) > 0L) {
            queryWrapper.eq("category_id", categoryId);
        }
    }

}