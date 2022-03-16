package com.hlju.onlineshop.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.coupon.entity.UserPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 10:19:10
 */
public interface UserPriceService extends IService<UserPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

