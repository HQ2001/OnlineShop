package com.hlju.onlineshop.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.user.entity.UserReceiveAddressEntity;

import java.util.Map;

/**
 * 会员收货地址
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
public interface UserReceiveAddressService extends IService<UserReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

