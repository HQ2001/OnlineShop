package com.hlju.onlineshop.goods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.hlju.onlineshop.goods.dao.CategoryDao;
import com.hlju.onlineshop.goods.entity.CategoryEntity;
import com.hlju.onlineshop.goods.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    public CategoryServiceImpl() {

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
     * 获取子分类
     *
     * @param currentCategory 当前分类
     * @param allCategory     所有分类的列表
     * @return 子分类
     */
    private List<CategoryEntity> getChildren(CategoryEntity currentCategory, List<CategoryEntity> allCategory) {
        return allCategory.stream()
                .filter(category -> currentCategory.getCatId().equals(category.getParentCid()))
                .peek(menu -> menu.setChildren(this.getChildren(menu, allCategory)))
                .sorted(Comparator.comparing(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

}