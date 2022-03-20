package com.hlju.onlineshop.goods.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.GoodAttrValueDao;
import com.hlju.onlineshop.goods.entity.GoodAttrValueEntity;
import com.hlju.onlineshop.goods.service.GoodAttrValueService;


@Service("goodAttrValueService")
public class GoodAttrValueServiceImpl extends ServiceImpl<GoodAttrValueDao, GoodAttrValueEntity> implements GoodAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GoodAttrValueEntity> page = this.page(
                new Query<GoodAttrValueEntity>().getPage(params),
                new QueryWrapper<GoodAttrValueEntity>()
        );

        return new PageUtils(page);
    }

}