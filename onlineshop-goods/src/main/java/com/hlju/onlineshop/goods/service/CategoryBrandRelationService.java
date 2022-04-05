package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.entity.BrandEntity;
import com.hlju.onlineshop.goods.entity.CategoryBrandRelationEntity;
import com.hlju.onlineshop.goods.entity.CategoryEntity;
import com.hlju.onlineshop.goods.vo.CategoryBrandRelationVO;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存品牌、分类关联关系详细信息
     * @param categoryBrandRelation 关联关系实体类
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 更新品牌、分类关系表中的品牌信息
     * @param brand 品牌实体
     */
    void updateBrand(BrandEntity brand);

    /**
     * 根据分类id查询所有品牌
     * @param categoryId 分类id
     * @return relation实体
     */
    List<BrandEntity> listByCategoryId(Long categoryId);
}

