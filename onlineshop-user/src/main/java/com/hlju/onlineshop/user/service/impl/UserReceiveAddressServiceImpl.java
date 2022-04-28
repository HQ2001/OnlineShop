package com.hlju.onlineshop.user.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.user.dao.UserReceiveAddressDao;
import com.hlju.onlineshop.user.entity.UserReceiveAddressEntity;
import com.hlju.onlineshop.user.service.UserReceiveAddressService;


@Service("userReceiveAddressService")
public class UserReceiveAddressServiceImpl extends ServiceImpl<UserReceiveAddressDao, UserReceiveAddressEntity> implements UserReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserReceiveAddressEntity> page = this.page(
                new Query<UserReceiveAddressEntity>().getPage(params),
                new QueryWrapper<UserReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<UserReceiveAddressEntity> listByUserId(Long userId) {
        return baseMapper.listByUserId(userId);
    }

}