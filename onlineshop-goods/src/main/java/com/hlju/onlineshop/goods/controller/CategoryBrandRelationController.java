package com.hlju.onlineshop.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hlju.onlineshop.goods.entity.BrandEntity;
import com.hlju.onlineshop.goods.vo.CategoryBrandRelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.goods.entity.CategoryBrandRelationEntity;
import com.hlju.onlineshop.goods.service.CategoryBrandRelationService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 品牌分类关联
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/category-brand-relation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 获取当前品牌关联的所有分类
     * @param brandId 品牌id
     * @return 品牌和分类关系实体List
     */
    @GetMapping("/category/list")
    public R categoryList(@RequestParam("brandId") Long brandId) {
        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId)
        );

        return R.ok().put("data", data);
    }

    /**
     * 获取当前品牌关联的所有分类
     * @param categoryId 品牌id
     * @return 品牌和分类关系实体List
     */
    @GetMapping("/brand/list")
    public R brandList(@RequestParam("categoryId") Long categoryId) {
        List<BrandEntity> data = categoryBrandRelationService.listByCategoryId(categoryId);

        List<CategoryBrandRelationVO> vos = data.stream()
                .map(brand -> {
                    CategoryBrandRelationVO vo = new CategoryBrandRelationVO();
                    vo.setBrandId(brand.getBrandId());
                    vo.setBrandName(brand.getName());
                    return vo;
                }).collect(Collectors.toList());

        return R.ok().put("data", vos);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
