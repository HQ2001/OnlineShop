package com.hlju.common.exception;

import lombok.NoArgsConstructor;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/3 17:08
 */
@NoArgsConstructor
public class UserInputException extends Exception {
    public UserInputException(String msg) {
        super(msg);
    }
}
