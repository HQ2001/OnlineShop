package com.hlju.onlineshop.goods.service.impl;

import com.hlju.onlineshop.goods.dto.AttrAttrGroupRelationDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public void saveBatch(List<AttrAttrGroupRelationDTO> relations) {
        List<AttrAttrGroupRelationEntity> relationEntities = relations.stream().map(item -> {
            AttrAttrGroupRelationEntity relationEntity = new AttrAttrGroupRelationEntity();
            relationEntity.setAttrId(item.getAttrId());
            relationEntity.setAttrGroupId(item.getAttrGroupId());
            return relationEntity;
        }).collect(Collectors.toList());
        baseMapper.deleteByRelationList(relations);
        this.saveBatch(relationEntities);
    }

}