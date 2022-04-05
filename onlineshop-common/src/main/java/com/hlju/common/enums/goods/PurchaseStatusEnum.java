package com.hlju.common.enums.goods;

import com.hlju.common.enums.BaseEnum;
import lombok.AllArgsConstructor;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/3 8:43
 */
@AllArgsConstructor
public enum PurchaseStatusEnum implements BaseEnum<Integer, String> {
    CREATED(0, "新建"),
    ASSIGNED(1, "已分配"),
    RECEIVED(2, "已领取"),
    DONE(3, "已完成"),
    HAS_EXCEPTION(4, "有异常"),
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
