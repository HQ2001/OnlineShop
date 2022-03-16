package com.hlju.onlineshop.user.dao;

import com.hlju.onlineshop.user.entity.UserLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
@Mapper
public interface UserLoginLogDao extends BaseMapper<UserLoginLogEntity> {

}
