package com.hlju.onlineshop.thirdparty.controller;

import com.hlju.common.utils.R;
import com.hlju.onlineshop.thirdparty.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/21 11:08
 */
@Controller
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    SmsService smsService;

    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone,
                      @RequestParam("code") String code) {
        smsService.sendSmsCode(phone, code);
        return R.ok();
    }
}
