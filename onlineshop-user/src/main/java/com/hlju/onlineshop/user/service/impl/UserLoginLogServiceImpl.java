package com.hlju.onlineshop.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.user.dao.UserLoginLogDao;
import com.hlju.onlineshop.user.entity.UserLoginLogEntity;
import com.hlju.onlineshop.user.service.UserLoginLogService;


@Service("userLoginLogService")
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogDao, UserLoginLogEntity> implements UserLoginLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserLoginLogEntity> page = this.page(
                new Query<UserLoginLogEntity>().getPage(params),
                new QueryWrapper<UserLoginLogEntity>()
        );

        return new PageUtils(page);
    }

}