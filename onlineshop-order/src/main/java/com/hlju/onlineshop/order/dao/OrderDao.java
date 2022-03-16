package com.hlju.onlineshop.order.dao;

import com.hlju.onlineshop.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 10:42:59
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

}
