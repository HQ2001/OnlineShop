package com.hlju.onlineshop.goods.service.impl;

import com.google.common.collect.Lists;
import com.hlju.onlineshop.goods.dao.CategoryBrandRelationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.CategoryDao;
import com.hlju.onlineshop.goods.entity.CategoryEntity;
import com.hlju.onlineshop.goods.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    private final CategoryBrandRelationDao categoryBrandRelationDao;

    @Autowired
    public CategoryServiceImpl(CategoryBrandRelationDao categoryBrandRelationDao) {
        this.categoryBrandRelationDao = categoryBrandRelationDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listByTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> level1List = entities.stream()
                .filter(item -> 1 == item.getCatLevel())
                .collect(Collectors.toList());
        // List<CategoryEntity> level2List = entities.stream()
        //         .filter(item -> 2 == item.getCatLevel())
        //         .collect(Collectors.toList());
        // List<CategoryEntity> level3List = entities.stream()
        //         .filter(item -> 3 == item.getCatLevel())
        //         .collect(Collectors.toList());
        // level1List.forEach(parent -> {
        //     List<CategoryEntity> children = level2List.stream()
        //             .filter(child -> parent.getCatId().equals(child.getParentCid()))
        //             .collect(Collectors.toList());
        //     parent.setChildren(children);
        // });
        // level2List.forEach(parent -> {
        //     List<CategoryEntity> children = level3List.stream()
        //             .filter(child -> parent.getCatId().equals(child.getParentCid()))
        //             .collect(Collectors.toList());
        //     parent.setChildren(children);
        // });
        return level1List.stream()
                .peek(menu -> menu.setChildren(this.getChildren(menu, entities)))
                .sorted(Comparator.comparing(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    /**
     * ???????????????
     *
     * @param currentCategory ????????????
     * @param allCategory     ?????????????????????
     * @return ?????????
     */
    private List<CategoryEntity> getChildren(CategoryEntity currentCategory, List<CategoryEntity> allCategory) {
        return allCategory.stream()
                .filter(category -> currentCategory.getCatId().equals(category.getParentCid()))
                .peek(menu -> menu.setChildren(this.getChildren(menu, allCategory)))
                .sorted(Comparator.comparing(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> catIds) {
        // TODO ????????????????????????????????????????????????
        baseMapper.deleteBatchIds(catIds);
    }

    @Override
    public List<Long> getCategoryPathById(Long catId) {
        List<Long> path = Lists.newArrayList();

        findParentPath(catId, path);

        Collections.reverse(path);
        return path;
    }

    private void findParentPath(Long catId, List<Long> path) {
        CategoryEntity category = baseMapper.selectById(catId);
        path.add(catId);
        Long parentCid = category.getParentCid();
        if (parentCid != 0) {
            findParentPath(category.getParentCid(), path);
        }
    }

    @Transactional
    @Override
    public void updateDetail(CategoryEntity category) {
        baseMapper.updateById(category);
        categoryBrandRelationDao.updateCategory(category);
    }

}