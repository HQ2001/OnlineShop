package com.hlju.onlineshop.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.AttrAttrGroupRelationDao;
import com.hlju.onlineshop.goods.entity.AttrAttrGroupRelationEntity;
import com.hlju.onlineshop.goods.service.AttrAttrGroupRelationService;


@Service("attrAttrGroupRelationService")
public class AttrAttrGroupRelationServiceImpl extends ServiceImpl<AttrAttrGroupRelationDao, AttrAttrGroupRelationEntity> implements AttrAttrGroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrGroupRelationEntity> page = this.page(
                new Query<AttrAttrGroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrGroupRelationEntity>()
        );

        return new PageUtils(page);
    }

}