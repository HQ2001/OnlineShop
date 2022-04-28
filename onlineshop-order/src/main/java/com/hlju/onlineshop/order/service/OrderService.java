package com.hlju.onlineshop.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.order.entity.OrderEntity;
import com.hlju.onlineshop.order.vo.OrderConfirmVO;

import java.util.Map;

/**
 * 订单
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 10:42:59
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 返回订单确认页用到的数据
     * @return
     */
    OrderConfirmVO getConfirmOrder() throws InterruptedException;
}

