package com.hlju.onlineshop.auth.dto;

import lombok.Data;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/22 19:01
 */
@Data
public class UserLoginDTO {

    /**
     * 登陆账号
     */
    private String loginAccount;
    /**
     * 登陆密码
     */
    private String password;

}
