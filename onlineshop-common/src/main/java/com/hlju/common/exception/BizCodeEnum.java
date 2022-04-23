package com.hlju.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 10: 通用
 * 11: 商品
 * 12: 订单
 * 13: 购物车
 * 14: 用户
 */
@Getter
@AllArgsConstructor
public enum BizCodeEnum {
    UNKNOWN_EXCEPTION(10000, "未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    SMS_CODE_EXCEPTION(10002, "验证码获取频率太高"),
    GOODS_UP_EXCEPTION(11000, "商品上架失败"),
    USER_EXISTS_EXCEPTION(14001, "用户已存在"),
    MOBILE_REGISTERED_EXCEPTION(14002, "电话已注册"),
    LOGIN_ACCOUNT_PASSWORD_INVALID_EXCEPTION(14003, "账号或密码错误"),
    ;
    private final Integer code;

    private final String msg;
}
