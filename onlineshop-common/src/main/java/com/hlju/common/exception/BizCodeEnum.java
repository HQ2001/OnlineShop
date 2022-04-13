package com.hlju.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizCodeEnum {
    UNKNOWN_EXCEPTION(10000, "未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    GOODS_UP_EXCEPTION(11000, "商品上架失败"),
    ;
    private final Integer code;

    private final String msg;
}
