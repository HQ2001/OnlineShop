package com.hlju.onlineshop.coupon.dao;

import com.hlju.onlineshop.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 10:19:10
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {

}
