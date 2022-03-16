package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

