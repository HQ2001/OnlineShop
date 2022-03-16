package com.hlju.onlineshop.goods.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hlju.onlineshop.goods.entity.GoodAttrValueEntity;
import com.hlju.onlineshop.goods.service.GoodAttrValueService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * spu属性值
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/goodattrvalue")
public class GoodAttrValueController {
    @Autowired
    private GoodAttrValueService goodAttrValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = goodAttrValueService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
            GoodAttrValueEntity goodAttrValue = goodAttrValueService.getById(id);

        return R.ok().put("goodAttrValue", goodAttrValue);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody GoodAttrValueEntity goodAttrValue) {
            goodAttrValueService.save(goodAttrValue);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody GoodAttrValueEntity goodAttrValue) {
            goodAttrValueService.updateById(goodAttrValue);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
            goodAttrValueService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
