package com.hlju.common.constant;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 21:08
 */
public class CartConstant {

    public static final String TEMP_USER_COOKIE_NAME = "user-key";
    /**
     * 过期时间（30天）
     */
    public static final int TEMP_USER_COOKIE_TIMEOUT = 60 * 60 * 24 * 30;

    /**
     * 购物车存放进redis中的前缀
     */
    public static final String CART_PREFIX = "cart:";

}
