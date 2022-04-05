package com.hlju.onlineshop.goods.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.hlju.onlineshop.goods.dao.AttrAttrGroupRelationDao;
import com.hlju.onlineshop.goods.dao.AttrGroupDao;
import com.hlju.onlineshop.goods.dao.CategoryDao;
import com.hlju.onlineshop.goods.dto.AttrAttrGroupRelationDTO;
import com.hlju.onlineshop.goods.entity.AttrAttrGroupRelationEntity;
import com.hlju.onlineshop.goods.dto.AttrDTO;
import com.hlju.onlineshop.goods.entity.AttrGroupEntity;
import com.hlju.onlineshop.goods.entity.CategoryEntity;
import com.hlju.common.enums.goods.AttrTypeEnum;
import com.hlju.onlineshop.goods.service.CategoryService;
import com.hlju.onlineshop.goods.vo.AttrVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.AttrDao;
import com.hlju.onlineshop.goods.entity.AttrEntity;
import com.hlju.onlineshop.goods.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    private final AttrAttrGroupRelationDao relationDao;
    private final AttrGroupDao attrGroupDao;
    private final CategoryDao categoryDao;
    private final CategoryService categoryService;

    @Autowired
    AttrServiceImpl(AttrAttrGroupRelationDao relationDao,
                    AttrGroupDao attrGroupDao,
                    CategoryDao categoryDao,
                    CategoryService categoryService) {
        this.relationDao = relationDao;
        this.attrGroupDao = attrGroupDao;
        this.categoryDao = categoryDao;
        this.categoryService = categoryService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrDTO attrDto) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrDto, attrEntity);
        this.save(attrEntity);

        if (Objects.equals(attrDto.getAttrType(), AttrTypeEnum.SALE_ATTR.getCode())
                || Objects.isNull(attrDto.getAttrGroupId())) {
            return;
        }

        AttrAttrGroupRelationEntity relationEntity = new AttrAttrGroupRelationEntity();
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attrDto.getAttrGroupId());
        relationDao.insert(relationEntity);
    }

    @Transactional
    @Override
    public void updateAttrById(AttrDTO attrDto) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrDto, attrEntity);
        this.updateById(attrEntity);

        if (Objects.equals(attrDto.getAttrType(), AttrTypeEnum.SALE_ATTR.getCode())) {
            return;
        }

        AttrAttrGroupRelationEntity relationEntity = relationDao.selectOneByAttrId(attrEntity.getAttrId());
        if (Objects.isNull(relationEntity)) {
            relationEntity = new AttrAttrGroupRelationEntity();
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationEntity.setAttrGroupId(attrDto.getAttrGroupId());
            relationDao.insert(relationEntity);
        } else {
            relationEntity.setAttrGroupId(attrDto.getAttrGroupId());
            relationDao.updateById(relationEntity);
        }
    }

    @Override
    public PageUtils queryPageByCategoryId(Map<String, Object> params, Long categoryId, Integer attrType) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type", attrType);
        if (categoryId != 0) {
            queryWrapper.eq("category_id", categoryId);
        }
        this.setAttrFuzzyQueryCondition(params, queryWrapper);

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        List<AttrEntity> records = page.getRecords();
        List<Long> attrIds = records.stream().map(AttrEntity::getAttrId).collect(Collectors.toList());
        Map<Long, Long> attrIdAndAttrGroupIdMap = Maps.newHashMap();
        Map<Long, String> attrGroupIdAndNameMap = Maps.newHashMap();
        // 如果为基础属性，才查询属性分组
        if (Objects.equals(attrType, AttrTypeEnum.BASE_ATTR.getCode())) {
            relationDao.selectList(new QueryWrapper<AttrAttrGroupRelationEntity>().in("attr_id", attrIds))
                    .forEach(relation -> {
                        if (!attrIdAndAttrGroupIdMap.containsKey(relation.getAttrId())) {
                            attrIdAndAttrGroupIdMap.put(relation.getAttrId(), relation.getAttrGroupId());
                        }
                    });
            attrGroupIdAndNameMap = attrGroupDao.selectBatchIds(attrIdAndAttrGroupIdMap.values())
                    .stream()
                    .collect(Collectors.toMap(
                            AttrGroupEntity::getAttrGroupId,
                            AttrGroupEntity::getAttrGroupName
                    ));
        }
        List<Long> categoryIds = records.stream().map(AttrEntity::getCategoryId).collect(Collectors.toList());
        Map<Long, String> categoryIdAndNameMap = categoryDao.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(CategoryEntity::getCatId, CategoryEntity::getName));
        Map<Long, String> finalAttrGroupIdAndNameMap = attrGroupIdAndNameMap;
        List<AttrVO> attrVoList = records.stream().map(attrEntity -> {
            AttrVO attrVO = new AttrVO();
            BeanUtils.copyProperties(attrEntity, attrVO);
            attrVO.setCategoryName(categoryIdAndNameMap.get(attrVO.getCategoryId()));
            Long attrGroupId = attrIdAndAttrGroupIdMap.get(attrVO.getAttrId());
            attrVO.setGroupName(finalAttrGroupIdAndNameMap.get(attrGroupId));
            return attrVO;
        }).collect(Collectors.toList());

        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(attrVoList);
        return pageUtils;
    }

    @Override
    public AttrVO getAttrInfoById(Long attrId) {
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        AttrVO attrVo = new AttrVO();
        BeanUtils.copyProperties(attrEntity, attrVo);
        // 分组信息
        AttrAttrGroupRelationEntity relation =
                relationDao.selectOne(new QueryWrapper<AttrAttrGroupRelationEntity>().eq("attr_id", attrId));
        if (Objects.nonNull(relation)) {
            Long attrGroupId = relation.getAttrGroupId();
            attrVo.setAttrGroupId(attrGroupId);
            AttrGroupEntity attrGroup = attrGroupDao.selectById(attrGroupId);
            if (Objects.nonNull(attrGroup)) {
                attrVo.setGroupName(attrGroup.getAttrGroupName());
            }
        }

        // 分类信息
        attrVo.setCategoryPath(categoryService.getCategoryPathById(attrVo.getCategoryId()));
        return attrVo;
    }

    @Override
    public List<AttrEntity> getAttrByAttrGroupId(Long attrGroupId) {
        List<AttrAttrGroupRelationEntity> relations = relationDao.listByAttrGroupId(attrGroupId);
        List<Long> attrIds = relations.stream()
                .map(AttrAttrGroupRelationEntity::getAttrId)
                .collect(Collectors.toList());
        return baseMapper.selectBatchIds(attrIds);
    }

    @Override
    public void removeByRelation(List<AttrAttrGroupRelationDTO> relationDtoList) {
        relationDao.deleteByRelationList(relationDtoList);
    }

    @Override
    public PageUtils getNoAttrRelations(Map<String, Object> params, Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupDao.selectById(attrGroupId);
        Long categoryId = attrGroup.getCategoryId();
        // 当前分类下其他分组的id
        List<Long> attrGroupIds = attrGroupDao.listByCategoryId(categoryId).stream()
                .map(AttrGroupEntity::getAttrGroupId)
                // .filter(groupId -> !Objects.equals(groupId, attrGroupId))
                .collect(Collectors.toList());
        // 根据分组找到相关联的属性
        List<Long> attrIds = relationDao.listByAttrGroupIds(attrGroupIds)
                .stream()
                .map(AttrAttrGroupRelationEntity::getAttrId)
                .collect(Collectors.toList());
        // 排除其他分组关联的属性
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
                .eq("category_id", categoryId)
                .eq("attr_type", AttrTypeEnum.BASE_ATTR.getCode());
        if (CollectionUtils.isNotEmpty(attrIds)) {
            queryWrapper.notIn("attr_id", attrIds);
        }
        this.setAttrFuzzyQueryCondition(params, queryWrapper);
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    /**
     * 设置属性混合查询的条件
     *
     * @param params       分页参数
     * @param queryWrapper 查询的queryWrapper
     */
    private void setAttrFuzzyQueryCondition(Map<String, Object> params, QueryWrapper<AttrEntity> queryWrapper) {
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }
    }

}