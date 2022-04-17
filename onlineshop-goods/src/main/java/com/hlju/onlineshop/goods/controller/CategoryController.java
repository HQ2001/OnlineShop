package com.hlju.onlineshop.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.goods.entity.CategoryEntity;
import com.hlju.onlineshop.goods.service.CategoryService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 商品三级分类
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 以树形结构查询出所有分类
     */
    @RequestMapping("/list-tree")
    public R list() {
        List<CategoryEntity> entities = categoryService.listByTree();

        return R.ok().put("data", entities);
    }

    @GetMapping("/list-by-ids")
    public R categoriesByCategoryIds(@RequestParam("categoryIds") List<Long> categoryIds) {
        List<CategoryEntity> categories = categoryService.listByIds(categoryIds);

        return R.ok().put("categories", categories);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateDetail(category);

        return R.ok();
    }

    /**
     * 批量修改排序
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody List<CategoryEntity> categoryList) {
        categoryService.updateBatchById(categoryList);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {

        categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
