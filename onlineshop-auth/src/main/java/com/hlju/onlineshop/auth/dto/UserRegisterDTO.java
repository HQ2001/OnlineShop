package com.hlju.onlineshop.auth.dto;

import com.hlju.common.valid.ValidateUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/21 15:04
 */
@Data
public class UserRegisterDTO {

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不可为空")
    @Length(min = 4, max = 20, message = "用户名必须是6-20位字符")
    private String userName;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不可为空")
    @Length(min = 6, max = 20, message = "密码必须是6-20位字符")
    private String password;
    /**
     * 电话号
     */
    @NotEmpty(message = "验证码不可为空")
    @Pattern(regexp = ValidateUtils.REGEX_MOBILE, message = "手机号格式错误")
    private String phone;
    /**
     * 验证码
     */
    @NotEmpty(message = "验证码不可为空")
    private String code;
}
