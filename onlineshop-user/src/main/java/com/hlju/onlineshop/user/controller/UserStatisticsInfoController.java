package com.hlju.onlineshop.user.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.user.entity.UserStatisticsInfoEntity;
import com.hlju.onlineshop.user.service.UserStatisticsInfoService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 会员统计信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
@RestController
@RequestMapping("/user/userstatisticsinfo")
public class UserStatisticsInfoController {
    @Autowired
    private UserStatisticsInfoService userStatisticsInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userStatisticsInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        UserStatisticsInfoEntity userStatisticsInfo = userStatisticsInfoService.getById(id);

        return R.ok().put("userStatisticsInfo", userStatisticsInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody UserStatisticsInfoEntity userStatisticsInfo) {
        userStatisticsInfoService.save(userStatisticsInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody UserStatisticsInfoEntity userStatisticsInfo) {
        userStatisticsInfoService.updateById(userStatisticsInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userStatisticsInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
