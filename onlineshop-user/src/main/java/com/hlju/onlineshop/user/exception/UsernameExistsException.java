package com.hlju.onlineshop.user.exception;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/22 15:25
 */
public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException() {
        super("用户名存在");
    }
}
