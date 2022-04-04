package com.hlju.onlineshop.user.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.user.entity.UserLoginLogEntity;
import com.hlju.onlineshop.user.service.UserLoginLogService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 会员登录记录
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
@RestController
@RequestMapping("/user/userloginlog")
public class UserLoginLogController {
    @Autowired
    private UserLoginLogService userLoginLogService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userLoginLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        UserLoginLogEntity userLoginLog = userLoginLogService.getById(id);

        return R.ok().put("userLoginLog", userLoginLog);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody UserLoginLogEntity userLoginLog) {
        userLoginLogService.save(userLoginLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody UserLoginLogEntity userLoginLog) {
        userLoginLogService.updateById(userLoginLog);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userLoginLogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
