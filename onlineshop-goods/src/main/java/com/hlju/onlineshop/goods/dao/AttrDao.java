package com.hlju.onlineshop.goods.dao;

import com.hlju.onlineshop.goods.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    /**
     * 根据searchType和attrIds查询出属性
     * @param searchType searchType
     * @param attrIds ids
     * @return 实体List
     */
    List<AttrEntity> listBySearchTypeAndAttrIds(@Param("searchType") Integer searchType,
                                                @Param("attrIds") List<Long> attrIds);
}
