package com.hlju.onlineshop.user.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.user.entity.UserCollectSubjectEntity;
import com.hlju.onlineshop.user.service.UserCollectSubjectService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 会员收藏的专题活动
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
@RestController
@RequestMapping("/user/usercollectsubject")
public class UserCollectSubjectController {
    @Autowired
    private UserCollectSubjectService userCollectSubjectService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userCollectSubjectService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        UserCollectSubjectEntity userCollectSubject = userCollectSubjectService.getById(id);

        return R.ok().put("userCollectSubject", userCollectSubject);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody UserCollectSubjectEntity userCollectSubject) {
        userCollectSubjectService.save(userCollectSubject);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody UserCollectSubjectEntity userCollectSubject) {
        userCollectSubjectService.updateById(userCollectSubject);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userCollectSubjectService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
