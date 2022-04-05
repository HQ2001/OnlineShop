package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.dto.AttrAttrGroupRelationDTO;
import com.hlju.onlineshop.goods.entity.AttrEntity;
import com.hlju.onlineshop.goods.dto.AttrDTO;
import com.hlju.onlineshop.goods.vo.AttrVO;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 新增属性，同时保存属性、属性分组关联表
     * @param attrDto dto
     */
    void saveAttr(AttrDTO attrDto);

    /**
     * 新增属性，同时保存属性、属性分组关联表
     * @param attrDto dto
     */
    void updateAttrById(AttrDTO attrDto);

    /**
     * 根据分类id查询相应的属性
     * @param params 分页参数
     * @param categoryId 分类id
     * @param attrType 属性分类【0-基本属性，1-销售属性】
     * @return page
     */
    PageUtils queryPageByCategoryId(Map<String, Object> params, Long categoryId, Integer attrType);

    /**
     * 根据属性id查询出详细信息，包含分组以及分类
     * @param attrId id
     * @return vo
     */
    AttrVO getAttrInfoById(Long attrId);

    /**
     * 根据属性分组id查询出所有的属性
     * @param attrGroupId 属性分组id
     * @return 属性实体list
     */
    List<AttrEntity> getAttrByAttrGroupId(Long attrGroupId);

    /**
     * 根据属性id和属性分组id删除
     * @param relationDtoList dto
     */
    void removeByRelation(List<AttrAttrGroupRelationDTO> relationDtoList);

    /**
     * 获取未关联的属性分页
     *  - 当前分组只能关联自己所属分类中的属性
     *  - 当前分组只能关联别的分组没有引用的属性
     * @param params 分页条件
     * @param attrGroupId 分组id
     * @return page
     */
    PageUtils getNoAttrRelations(Map<String, Object> params, Long attrGroupId);
}

