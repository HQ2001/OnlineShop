package com.hlju.onlineshop.order.dao;

import com.hlju.onlineshop.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 10:42:59
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {

}
