package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.entity.GoodAttrValueEntity;

import java.util.Map;

/**
 * spu属性值
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface GoodAttrValueService extends IService<GoodAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

