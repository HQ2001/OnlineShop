package com.hlju.onlineshop.user.exception;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/22 15:25
 */
public class MobileExistsException extends RuntimeException {
    public MobileExistsException() {
        super("手机号存在");
    }
}
