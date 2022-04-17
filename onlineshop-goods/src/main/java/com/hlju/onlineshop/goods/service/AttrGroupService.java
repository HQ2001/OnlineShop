package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.entity.AttrGroupEntity;
import com.hlju.onlineshop.goods.vo.AttrGroupWithAttrsVO;
import com.hlju.onlineshop.goods.vo.SkuInfoDetailVO;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据categoryId分页查询数据
     * @param params 包括分页信息
     * @param categoryId 分类id，为0的话表示查询所有
     * @return 查询结果
     */
    PageUtils queryPageByCategoryId(Map<String, Object> params, Long categoryId);

    /**
     * 根据分类id查询出属性分组以及属性分组下的属性
     * @param categoryId 分类id
     * @return vo的list
     */
    List<AttrGroupWithAttrsVO> getAttrGroupWithAttrsByCategoryId(Long categoryId);

    /**
     * 根据spuId查询出当前商品对应的所有属性分组信息，包含属性分组下的基础属性
     * @param spuId spuId
     * @return vo
     */
    List<SkuInfoDetailVO.AttrGroupVO> getAttrGroupWithAttrsBySpuId(Long spuId);
}

