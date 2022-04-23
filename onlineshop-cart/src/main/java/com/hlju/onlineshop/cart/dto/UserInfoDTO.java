package com.hlju.onlineshop.cart.dto;

import lombok.Data;

/**
 * 标识用户登陆状态的dto
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 21:03
 */
@Data
public class UserInfoDTO {

    /**
     * 登录用户的id
     */
    private Long userId;

    /**
     * 未登录用户的user-key
     */
    private String userKey;

    /**
     * 是否临时用户
     */
    private Boolean isTempUser = true;

}
