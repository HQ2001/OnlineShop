package com.hlju.onlineshop.user.dao;

import com.hlju.onlineshop.user.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会员
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    int countUserName(@Param("userName") String userName);

    int countMobile(@Param("mobile") String mobile);

    /**
     * 根据登录账号进行查询
     * @param loginAccount 登陆的账号（用户名或手机号）
     * @return 实体类
     */
    UserEntity selectByLoginAccount(@Param("loginAccount") String loginAccount);
}
