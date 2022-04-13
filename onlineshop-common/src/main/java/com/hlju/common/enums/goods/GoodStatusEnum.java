package com.hlju.common.enums.goods;

import com.hlju.common.enums.BaseEnum;
import lombok.AllArgsConstructor;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/10 9:56
 */
@AllArgsConstructor
public enum GoodStatusEnum implements BaseEnum<Integer, String> {
    CREATED(0, "新建"),
    UP(1, "商品上架"),
    DOWN(2, "商品下架"),
    ;

    private final Integer code;

    private final String msg;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
