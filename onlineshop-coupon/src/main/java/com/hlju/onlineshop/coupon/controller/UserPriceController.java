package com.hlju.onlineshop.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hlju.onlineshop.coupon.entity.UserPriceEntity;
import com.hlju.onlineshop.coupon.service.UserPriceService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 商品会员价格
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 10:19:10
 */
@RestController
@RequestMapping("/coupon/userprice")
public class UserPriceController {
    @Autowired
    private UserPriceService userPriceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userPriceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
            UserPriceEntity userPrice = userPriceService.getById(id);

        return R.ok().put("userPrice", userPrice);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserPriceEntity userPrice) {
            userPriceService.save(userPrice);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserPriceEntity userPrice) {
            userPriceService.updateById(userPrice);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
            userPriceService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
