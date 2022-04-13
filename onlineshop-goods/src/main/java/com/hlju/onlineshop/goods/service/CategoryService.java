package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.entity.CategoryEntity;
import com.hlju.onlineshop.goods.vo.Catalog2VO;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 以树形结构查询出所有分类
     *
     * @return 树形结构，一级分类
     */
    List<CategoryEntity> listByTree();

    /**
     * 通过id删除相应菜单
     * @param catIds 分类id数组
     */
    void removeMenuByIds(List<Long> catIds);

    /**
     * 根据分类id查询出该分类的完整路径
     * @param catId 分类id
     * @return 分类的路径
     */
    List<Long> getCategoryPathById(Long catId);

    /**
     * 更新分类表以及其他包含分类信息的表
     * @param category 分类实体
     */
    void updateDetail(CategoryEntity category);

    /**
     * 获取1级分类
     * @param level 等级
     * @return 实体list
     */
    List<CategoryEntity> getLevel1Categories(int level);

    /**
     * 获取目录的json
     * @return k - 一级分类id
     */
    Map<String, List<Catalog2VO>> getCatalogJson();
}

