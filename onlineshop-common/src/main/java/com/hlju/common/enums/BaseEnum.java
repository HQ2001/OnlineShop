package com.hlju.common.enums;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/3/26 16:40
 */
public interface BaseEnum<K, V> {
    K getCode();

    V getMsg();
}
