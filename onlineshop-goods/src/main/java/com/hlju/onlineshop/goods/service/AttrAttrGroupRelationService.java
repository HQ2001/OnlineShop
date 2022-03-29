package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.dto.AttrAttrGroupRelationDTO;
import com.hlju.onlineshop.goods.entity.AttrAttrGroupRelationEntity;
import com.hlju.onlineshop.goods.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface AttrAttrGroupRelationService extends IService<AttrAttrGroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 批量保存关联关系
     * @param relations dto
     */
    void saveBatch(List<AttrAttrGroupRelationDTO> relations);
}

