package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.entity.GoodAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface GoodAttrValueService extends IService<GoodAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据spuId查询出所有属性值
     * @param spuId spuId
     * @return 实体list
     */
    List<GoodAttrValueEntity> baseAttrListForSpu(Long spuId);

    /**
     * 通过spuId更新基础属性（spu属性）
     * 全量更新
     * @param spuId spuId
     * @param entities 更新的实体类
     */
    void updateBaseAttrBySpuId(Long spuId, List<GoodAttrValueEntity> entities);
}

