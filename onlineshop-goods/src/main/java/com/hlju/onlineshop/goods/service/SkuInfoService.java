package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.entity.SkuInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据条件查询列表页
     * @param params 查询条件
     * @return page
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 向queryWrapper中添加查询条件
     * @param queryWrapper queryWrapper
     * @param params 条件的map
     */
    void setQueryCondition(QueryWrapper<?> queryWrapper, Map<String, Object> params);

    List<SkuInfoEntity> getSkusBySpuId(Long spuId);
}

