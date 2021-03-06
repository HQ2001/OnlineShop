package com.hlju.onlineshop.goods.service.impl;

import com.google.common.collect.Lists;
import com.hlju.common.utils.ListUtils;
import com.hlju.onlineshop.goods.dao.AttrAttrGroupRelationDao;
import com.hlju.onlineshop.goods.dao.AttrDao;
import com.hlju.onlineshop.goods.entity.AttrAttrGroupRelationEntity;
import com.hlju.onlineshop.goods.entity.AttrEntity;
import com.hlju.onlineshop.goods.vo.AttrGroupWithAttrsVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    @Autowired
    AttrGroupServiceImpl(AttrAttrGroupRelationDao relationDao,
                         AttrDao attrDao) {
        this.relationDao = relationDao;
        this.attrDao = attrDao;
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
        // ?????????????????????????????????
        List<AttrGroupEntity> attrGroups = baseMapper.listByCategoryId(categoryId);

        List<Long> attrGroupIds = attrGroups.stream()
                .map(AttrGroupEntity::getAttrGroupId)
                .collect(Collectors.toList());

        // ??????????????????????????????
        List<AttrAttrGroupRelationEntity> relations = relationDao.listByAttrGroupIds(attrGroupIds);
        // k - ????????????id       v - ??????????????????idList
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

        // k - ??????id   v - ????????????
        Map<Long, AttrEntity> attrMap = attrDao.selectBatchIds(attrIds).stream()
                .collect(Collectors.toMap(AttrEntity::getAttrId, item -> item));

        // k - ??????id   v - ????????????
        Map<Long, AttrGroupEntity> attrGroupMap = attrGroups.stream().collect(
                Collectors.toMap(AttrGroupEntity::getAttrGroupId, item -> item)
        );
        return attrGroupIds.stream()
                .map(groupId -> {
                    AttrGroupWithAttrsVO vo = new AttrGroupWithAttrsVO();
                    AttrGroupEntity groupEntity = attrGroupMap.get(groupId);
                    BeanUtils.copyProperties(groupEntity, vo);
                    // ?????????????????????
                    // ????????????????????????????????????id
                    List<Long> attrIdList = attrGroupIdAndAttrIdsMap.get(groupId);
                    List<AttrEntity> attrs = Lists.newArrayList();
                    if (!CollectionUtils.isEmpty(attrIdList)) {
                        attrIdList.forEach(attrId -> attrs.add(attrMap.get(attrId)));
                    }
                    vo.setAttrs(attrs);
                    return vo;
                }).collect(Collectors.toList());
    }

}