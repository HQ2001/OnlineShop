package com.hlju.onlineshop.user.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.user.entity.UserCollectSpuEntity;
import com.hlju.onlineshop.user.service.UserCollectSpuService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 会员收藏的商品
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
@RestController
@RequestMapping("/user/usercollectspu")
public class UserCollectSpuController {
    @Autowired
    private UserCollectSpuService userCollectSpuService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userCollectSpuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        UserCollectSpuEntity userCollectSpu = userCollectSpuService.getById(id);

        return R.ok().put("userCollectSpu", userCollectSpu);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody UserCollectSpuEntity userCollectSpu) {
        userCollectSpuService.save(userCollectSpu);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody UserCollectSpuEntity userCollectSpu) {
        userCollectSpuService.updateById(userCollectSpu);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userCollectSpuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
