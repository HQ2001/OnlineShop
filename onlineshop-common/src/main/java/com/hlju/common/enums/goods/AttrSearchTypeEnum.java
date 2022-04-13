package com.hlju.common.enums.goods;

import com.hlju.common.enums.BaseEnum;
import lombok.AllArgsConstructor;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/9 15:36
 */
@AllArgsConstructor
public enum AttrSearchTypeEnum implements BaseEnum<Integer, String> {
    NOT_SEARCH(0, "不检索"),
    SEARCH(1, "检索"),
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
