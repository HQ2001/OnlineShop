package com.hlju.onlineshop.coupon.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.coupon.dao.UserPriceDao;
import com.hlju.onlineshop.coupon.entity.UserPriceEntity;
import com.hlju.onlineshop.coupon.service.UserPriceService;


@Service("userPriceService")
public class UserPriceServiceImpl extends ServiceImpl<UserPriceDao, UserPriceEntity> implements UserPriceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserPriceEntity> page = this.page(
                new Query<UserPriceEntity>().getPage(params),
                new QueryWrapper<UserPriceEntity>()
        );

        return new PageUtils(page);
    }

}