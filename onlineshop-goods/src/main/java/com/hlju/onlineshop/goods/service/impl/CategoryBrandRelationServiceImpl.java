package com.hlju.onlineshop.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hlju.onlineshop.goods.dao.BrandDao;
import com.hlju.onlineshop.goods.dao.CategoryDao;
import com.hlju.onlineshop.goods.entity.BrandEntity;
import com.hlju.onlineshop.goods.entity.CategoryEntity;
import com.hlju.onlineshop.goods.vo.CategoryBrandRelationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.CategoryBrandRelationDao;
import com.hlju.onlineshop.goods.entity.CategoryBrandRelationEntity;
import com.hlju.onlineshop.goods.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    private final BrandDao brandDao;

    private final CategoryDao categoryDao;

    @Autowired
    CategoryBrandRelationServiceImpl(BrandDao brandDao,
                                     CategoryDao categoryDao) {
        this.brandDao = brandDao;
        this.categoryDao = categoryDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long categoryId = categoryBrandRelation.getCategoryId();

        BrandEntity brand = brandDao.selectById(brandId);
        CategoryEntity category = categoryDao.selectById(categoryId);

        categoryBrandRelation.setBrandName(brand.getName());
        categoryBrandRelation.setCategoryName(category.getName());

        baseMapper.insert(categoryBrandRelation);
    }

    @Override
    public void updateBrand(BrandEntity brand) {
        CategoryBrandRelationEntity relation = new CategoryBrandRelationEntity();
        relation.setBrandId(brand.getBrandId());
        relation.setBrandName(brand.getName());
        UpdateWrapper<CategoryBrandRelationEntity> updateWrapper =
                new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brand.getBrandId());
        baseMapper.update(relation, updateWrapper);
    }

    @Override
    public List<BrandEntity> listByCategoryId(Long categoryId) {
        List<CategoryBrandRelationEntity> relations = baseMapper.listByCategoryId(categoryId);
        List<Long> brandIds = relations.stream()
                .map(CategoryBrandRelationEntity::getBrandId)
                .collect(Collectors.toList());
        return brandDao.selectBatchIds(brandIds);
    }

}