package com.hlju.common.enums.goods;

import com.hlju.common.enums.BaseEnum;
import lombok.AllArgsConstructor;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/3/26 16:38
 */
@AllArgsConstructor
public enum AttrTypeEnum implements BaseEnum<Integer, String> {
    SALE_ATTR(0, "销售属性"),
    BASE_ATTR(1, "基础属性"),
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
