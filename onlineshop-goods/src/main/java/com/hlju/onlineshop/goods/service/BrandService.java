package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.entity.BrandEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新品牌表以及其他包含品牌信息的表
     * @param brand 品牌实体
     */
    void updateDetail(BrandEntity brand);

    /**
     * 根据ids查询实体
     * @param brandIds 品牌ids
     * @return 实体list
     */
    List<BrandEntity> listByIds(List<Long> brandIds);
}

