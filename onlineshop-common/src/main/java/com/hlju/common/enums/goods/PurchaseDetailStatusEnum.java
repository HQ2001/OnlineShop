package com.hlju.common.enums.goods;

import com.hlju.common.enums.BaseEnum;
import lombok.AllArgsConstructor;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/3 8:43
 */
@AllArgsConstructor
public enum PurchaseDetailStatusEnum implements BaseEnum<Integer, String> {
    CREATED(0, "新建"),
    ASSIGNED(1, "已分配"),
    BUYING(2, "正在采购"),
    DONE(3, "已完成"),
    FAILED(4, "采购失败"),
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
