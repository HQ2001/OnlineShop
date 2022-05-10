package com.hlju.onlineshop.goods.service.impl;

import com.google.common.collect.Lists;
import com.hlju.common.utils.ListUtils;
import com.hlju.onlineshop.goods.dao.AttrAttrGroupRelationDao;
import com.hlju.onlineshop.goods.dao.AttrDao;
import com.hlju.onlineshop.goods.entity.AttrAttrGroupRelationEntity;
import com.hlju.onlineshop.goods.entity.AttrEntity;
import com.hlju.onlineshop.goods.entity.GoodAttrValueEntity;
import com.hlju.onlineshop.goods.service.GoodAttrValueService;
import com.hlju.onlineshop.goods.vo.AttrGroupWithAttrsVO;
import com.hlju.onlineshop.goods.vo.SkuInfoDetailVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.AttrGroupDao;
import com.hlju.onlineshop.goods.entity.AttrGroupEntity;
import com.hlju.onlineshop.goods.service.AttrGroupService;
import org.springframework.util.CollectionUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    private final AttrAttrGroupRelationDao relationDao;
    private final AttrDao attrDao;
    private final GoodAttrValueService attrValueService;

    @Autowired
    AttrGroupServiceImpl(AttrAttrGroupRelationDao relationDao,
                         AttrDao attrDao,
                         GoodAttrValueService attrValueService) {
        this.relationDao = relationDao;
        this.attrDao = attrDao;
        this.attrValueService = attrValueService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCategoryId(Map<String, Object> params, Long categoryId) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (categoryId != 0) {
            wrapper.eq("category_id", categoryId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            wrapper.and(obj -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );
        page.setRecords(ListUtils.sortList(page.getRecords(), AttrGroupEntity::getSort));
        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupWithAttrsVO> getAttrGroupWithAttrsByCategoryId(Long categoryId) {
        // 查询分类下出所有的分组
        List<AttrGroupEntity> attrGroups = baseMapper.listByCategoryId(categoryId);

        List<Long> attrGroupIds = attrGroups.stream()
                .map(AttrGroupEntity::getAttrGroupId)
                .collect(Collectors.toList());

        // 查询出所有关联的属性
        List<AttrAttrGroupRelationEntity> relations = relationDao.listByAttrGroupIds(attrGroupIds);
        // k - 属性分组id       v - 分组下属性的idList
        Map<Long, List<Long>> attrGroupIdAndAttrIdsMap = relations.stream()
                .collect(Collectors.groupingBy(
                        AttrAttrGroupRelationEntity::getAttrGroupId,
                        Collectors.mapping(
                                AttrAttrGroupRelationEntity::getAttrId,
                                Collectors.toList()
                        )
                ));
        List<Long> attrIds = relations.stream()
                .map(AttrAttrGroupRelationEntity::getAttrId)
                .collect(Collectors.toList());

        // k - 属性id   v - 属性实体
        Map<Long, AttrEntity> attrMap = attrDao.selectBatchIds(attrIds).stream()
                .collect(Collectors.toMap(AttrEntity::getAttrId, item -> item));

        // k - 分组id   v - 分组实体
        Map<Long, AttrGroupEntity> attrGroupMap = attrGroups.stream().collect(
                Collectors.toMap(AttrGroupEntity::getAttrGroupId, item -> item)
        );
        return attrGroupIds.stream()
                .map(groupId -> {
                    AttrGroupWithAttrsVO vo = new AttrGroupWithAttrsVO();
                    AttrGroupEntity groupEntity = attrGroupMap.get(groupId);
                    BeanUtils.copyProperties(groupEntity, vo);
                    // 设置关联的属性
                    // 当前属性分组下的所有属性id
                    List<Long> attrIdList = attrGroupIdAndAttrIdsMap.get(groupId);
                    List<AttrEntity> attrs = Lists.newArrayList();
                    if (!CollectionUtils.isEmpty(attrIdList)) {
                        attrIdList.forEach(attrId -> attrs.add(attrMap.get(attrId)));
                    }
                    vo.setAttrs(attrs);
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SkuInfoDetailVO.AttrGroupVO> getAttrGroupWithAttrsBySpuId(Long spuId) {
        // 根据spuId查询出所有的属性值
        List<GoodAttrValueEntity> attrValues = attrValueService.baseAttrListForSpu(spuId);
        Map<Long, GoodAttrValueEntity> attrIdMap = attrValues.stream()
                .collect(Collectors.toMap(GoodAttrValueEntity::getAttrId, item -> item));
        // 根据查出来的属性id去【属性、属性分组】表中查出相应的分组id
        List<AttrAttrGroupRelationEntity> relations = relationDao.listByAttrIds(Lists.newArrayList(attrIdMap.keySet()));
        if (CollectionUtils.isEmpty(relations)) {
            return Lists.newArrayList();
        }
        Map<Long, List<Long>> attrGroupIdAndAttrIdsMap = relations.stream()
                .collect(Collectors.groupingBy(
                        AttrAttrGroupRelationEntity::getAttrGroupId,
                        Collectors.mapping(
                                AttrAttrGroupRelationEntity::getAttrId,
                                Collectors.toList()
                        )
                ));
        // 根据属性分组id查询出属性名
        List<AttrGroupEntity> groups = this.listByIds(attrGroupIdAndAttrIdsMap.keySet());
        List<SkuInfoDetailVO.AttrGroupVO> groupVOS = new ArrayList<>();
        groups.forEach(group -> {
            SkuInfoDetailVO.AttrGroupVO groupVO = new SkuInfoDetailVO.AttrGroupVO();
            groupVO.setGroupName(group.getAttrGroupName());
            List<SkuInfoDetailVO.BaseAttrVO> baseBaseAttrVOS = new ArrayList<>();
            // 当前属性组下所有属性id
            List<Long> attrIds = attrGroupIdAndAttrIdsMap.get(group.getAttrGroupId());
            if (Objects.isNull(attrIds)) {
                return;
            }
            attrIds.forEach(attrId -> {
                SkuInfoDetailVO.BaseAttrVO baseAttrVO = new SkuInfoDetailVO.BaseAttrVO();
                GoodAttrValueEntity attr = attrIdMap.get(attrId);
                if (Objects.isNull(attr)) {
                    return;
                }
                baseAttrVO.setAttrName(attr.getAttrName());
                baseAttrVO.setAttrValues(attr.getAttrValue());
                baseBaseAttrVOS.add(baseAttrVO);
            });
            groupVO.setBaseAttrs(baseBaseAttrVOS);
            groupVOS.add(groupVO);
        });
        return groupVOS;
    }

}