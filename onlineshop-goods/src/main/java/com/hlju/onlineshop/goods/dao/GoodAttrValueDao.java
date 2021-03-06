package com.hlju.onlineshop.goods.dao;

import com.hlju.onlineshop.goods.entity.GoodAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * spu属性值
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@Mapper
public interface GoodAttrValueDao extends BaseMapper<GoodAttrValueEntity> {

    List<GoodAttrValueEntity> listBySpuId(@Param("spuId") Long spuId);

    void deleteBySpuId(@Param("spuId") Long spuId);
}
