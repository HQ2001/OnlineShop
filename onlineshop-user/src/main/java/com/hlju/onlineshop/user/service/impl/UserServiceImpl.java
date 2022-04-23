package com.hlju.onlineshop.user.service.impl;

import com.hlju.onlineshop.user.dto.UserLoginDTO;
import com.hlju.onlineshop.user.dto.UserRegisterDTO;
import com.hlju.onlineshop.user.entity.UserLevelEntity;
import com.hlju.onlineshop.user.exception.MobileExistsException;
import com.hlju.onlineshop.user.exception.UsernameExistsException;
import com.hlju.onlineshop.user.service.UserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.user.dao.UserDao;
import com.hlju.onlineshop.user.entity.UserEntity;
import com.hlju.onlineshop.user.service.UserService;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    private final UserLevelService userLevelService;

    @Autowired
    UserServiceImpl(UserLevelService userLevelService) {
        this.userLevelService = userLevelService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(UserRegisterDTO dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(dto.getUserName());
        userEntity.setNickname(dto.getUserName());
        userEntity.setMobile(dto.getPhone());

        // 默认等级
        UserLevelEntity userLevelEntity = userLevelService.getDefaultLevel();
        userEntity.setLevelId(userLevelEntity.getId());

        // 检查用户名和手机号唯一
        checkUserNameUnique(dto.getUserName());
        checkMobileUnique(dto.getPhone());

        // 密码进行加密，使用spring提供的盐值加密
        String password = dto.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(passwordEncoder.encode(password));

        baseMapper.insert(userEntity);
    }

    @Override
    public void checkUserNameUnique(String userName) throws UsernameExistsException {
        int count = baseMapper.countUserName(userName);
        if (count > 0) {
            throw new UsernameExistsException();
        }
    }

    @Override
    public void checkMobileUnique(String mobile) throws MobileExistsException {
        int count = baseMapper.countMobile(mobile);
        if (count > 0) {
            throw new MobileExistsException();
        }
    }

    @Override
    public UserEntity login(UserLoginDTO dto) {
        UserEntity userEntity = baseMapper.selectByLoginAccount(dto.getLoginAccount());
        if (Objects.isNull(userEntity)) {
            return null;
        }
        String passwordDb = userEntity.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 匹配上了，登陆成功
        if (passwordEncoder.matches(dto.getPassword(), passwordDb)) {
            return userEntity;
        }
        return null;
    }

}