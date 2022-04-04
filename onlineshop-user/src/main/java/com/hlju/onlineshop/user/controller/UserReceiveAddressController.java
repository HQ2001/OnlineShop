package com.hlju.onlineshop.user.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.user.entity.UserReceiveAddressEntity;
import com.hlju.onlineshop.user.service.UserReceiveAddressService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 会员收货地址
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
@RestController
@RequestMapping("/user/userreceiveaddress")
public class UserReceiveAddressController {
    @Autowired
    private UserReceiveAddressService userReceiveAddressService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userReceiveAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        UserReceiveAddressEntity userReceiveAddress = userReceiveAddressService.getById(id);

        return R.ok().put("userReceiveAddress", userReceiveAddress);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody UserReceiveAddressEntity userReceiveAddress) {
        userReceiveAddressService.save(userReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody UserReceiveAddressEntity userReceiveAddress) {
        userReceiveAddressService.updateById(userReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
