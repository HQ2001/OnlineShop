package com.hlju.onlineshop.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.user.entity.UserCollectSpuEntity;

import java.util.Map;

/**
 * 会员收藏的商品
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
public interface UserCollectSpuService extends IService<UserCollectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

