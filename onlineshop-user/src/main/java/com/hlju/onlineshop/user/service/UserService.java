package com.hlju.onlineshop.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.user.dto.UserLoginDTO;
import com.hlju.onlineshop.user.dto.UserRegisterDTO;
import com.hlju.onlineshop.user.entity.UserEntity;
import com.hlju.onlineshop.user.exception.MobileExistsException;
import com.hlju.onlineshop.user.exception.UsernameExistsException;

import java.util.Map;

/**
 * 会员
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 注册会员
     * @param dto dto
     */
    void register(UserRegisterDTO dto);

    /**
     * 检查用户名是否唯一
     */
    void checkUserNameUnique(String userName) throws UsernameExistsException;

    /**
     * 检查有记号是否唯一
     */
    void checkMobileUnique(String mobile) throws MobileExistsException;

    UserEntity login(UserLoginDTO dto);
}

