package com.hlju.onlineshop.user.dto;

import com.hlju.common.valid.ValidateUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/22 15:06
 */
@Data
public class UserRegisterDTO {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 电话号
     */
    private String phone;
}
