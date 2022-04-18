package com.hlju.onlineshop.goods.dao;

import com.hlju.onlineshop.goods.dto.AttrAttrGroupRelationDTO;
import com.hlju.onlineshop.goods.entity.AttrAttrGroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@Mapper
public interface AttrAttrGroupRelationDao extends BaseMapper<AttrAttrGroupRelationEntity> {

    AttrAttrGroupRelationEntity selectByAttrIdAndAttrGroupId(@Param("attrId") Long attrId,
                                      @Param("attrGroupId") Long attrGroupId);

    AttrAttrGroupRelationEntity selectOneByAttrId(@Param("attrId") Long attrId);

    List<AttrAttrGroupRelationEntity> listByAttrGroupId(@Param("attrGroupId") Long attrGroupId);

    List<AttrAttrGroupRelationEntity> listByAttrGroupIds(@Param("list") List<Long> attrGroupId);

    void deleteByRelationList(@Param("list") List<AttrAttrGroupRelationDTO> relationDtoList);

    List<AttrAttrGroupRelationEntity> listByAttrIds(@Param("attrIds") List<Long> attrIds);
}
