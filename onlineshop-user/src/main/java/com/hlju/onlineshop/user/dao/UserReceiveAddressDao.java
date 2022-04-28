package com.hlju.onlineshop.user.dao;

import com.hlju.onlineshop.user.entity.UserReceiveAddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员收货地址
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
@Mapper
public interface UserReceiveAddressDao extends BaseMapper<UserReceiveAddressEntity> {

    List<UserReceiveAddressEntity> listByUserId(@Param("userId") Long userId);
}
