package com.hlju.onlineshop.goods.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.goods.entity.AttrAttrGroupRelationEntity;
import com.hlju.onlineshop.goods.service.AttrAttrGroupRelationService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 属性&属性分组关联
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/attrattrgrouprelation")
public class AttrAttrGroupRelationController {
    @Autowired
    private AttrAttrGroupRelationService attrAttrGroupRelationService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrAttrGroupRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        AttrAttrGroupRelationEntity attrAttrGroupRelation = attrAttrGroupRelationService.getById(id);

        return R.ok().put("attrAttrGroupRelation", attrAttrGroupRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody AttrAttrGroupRelationEntity attrAttrGroupRelation) {
        attrAttrGroupRelationService.save(attrAttrGroupRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody AttrAttrGroupRelationEntity attrAttrGroupRelation) {
        attrAttrGroupRelationService.updateById(attrAttrGroupRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        attrAttrGroupRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
