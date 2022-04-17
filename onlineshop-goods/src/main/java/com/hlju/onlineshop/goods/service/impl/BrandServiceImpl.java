package com.hlju.onlineshop.goods.service.impl;

import com.hlju.common.utils.ListUtils;
import com.hlju.onlineshop.goods.dao.CategoryBrandRelationDao;
import com.hlju.onlineshop.goods.entity.CategoryBrandRelationEntity;
import com.hlju.onlineshop.goods.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.BrandDao;
import com.hlju.onlineshop.goods.entity.BrandEntity;
import com.hlju.onlineshop.goods.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    private final CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    BrandServiceImpl(CategoryBrandRelationService categoryBrandRelationService) {
        this.categoryBrandRelationService = categoryBrandRelationService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            wrapper.eq("brand_id", key)
                    .or().like("name", key)
                    .or().like("description", key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                wrapper
        );
        page.setRecords(ListUtils.sortList(page.getRecords(), BrandEntity::getSort));
        return new PageUtils(page);

    }

    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {
        baseMapper.updateById(brand);
        if (StringUtils.isNotEmpty(brand.getName())) {
            categoryBrandRelationService.updateBrand(brand);
        }
    }

    @Cacheable(value = {"brands"}, key = "#root.methodName+#root.args")
    @Override
    public List<BrandEntity> listByIds(List<Long> brandIds)  {
        return baseMapper.listByIds(brandIds);
    }

}