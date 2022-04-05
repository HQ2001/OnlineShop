package com.hlju.onlineshop.goods.dao;

import com.hlju.onlineshop.goods.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hlju.onlineshop.goods.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 品牌分类关联
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {
    /**
     * 更新品牌、分类关系表中的分类信息
     * @param category 分类实体
     */
    void updateCategory(@Param("category") CategoryEntity category);

    List<CategoryBrandRelationEntity> listByBrandId(@Param("brandId") Long brandId);

    List<CategoryBrandRelationEntity> listByCategoryId(@Param("categoryId") Long categoryId);
}
