package com.hlju.onlineshop.goods.dao;

import com.hlju.onlineshop.goods.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 品牌
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {

    List<BrandEntity> listByIds(@Param("brandIds") List<Long> brandIds);
}
