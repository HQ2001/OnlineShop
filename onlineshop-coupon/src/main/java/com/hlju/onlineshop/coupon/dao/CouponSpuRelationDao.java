package com.hlju.onlineshop.coupon.dao;

import com.hlju.onlineshop.coupon.entity.CouponSpuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券与产品关联
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 10:19:10
 */
@Mapper
public interface CouponSpuRelationDao extends BaseMapper<CouponSpuRelationEntity> {

}
