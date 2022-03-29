package com.hlju.onlineshop.goods.controller;

import java.util.Arrays;
import java.util.Map;

import com.hlju.onlineshop.goods.dto.AttrDTO;
import com.hlju.onlineshop.goods.vo.AttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.goods.entity.AttrEntity;
import com.hlju.onlineshop.goods.service.AttrService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 商品属性
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 根据分类id查询列表
     */
    @GetMapping("/list/{categoryId}")
    public R baseList(@RequestParam Map<String, Object> params,
                      @PathVariable("categoryId") Long categoryId,
                      @RequestParam("attrType") Integer attrType) {
        PageUtils page = attrService.queryPageByCategoryId(params, categoryId, attrType);

        return R.ok().put("page", page);
    }


    /**
     * 详细信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrVO attrVo = attrService.getAttrInfoById(attrId);

        return R.ok().put("attr", attrVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrDTO attrDto) {
        attrService.saveAttr(attrDto);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrDTO attrDto) {
        attrService.updateAttrById(attrDto);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
