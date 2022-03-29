package com.hlju.onlineshop.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hlju.onlineshop.goods.dto.AttrAttrGroupRelationDTO;
import com.hlju.onlineshop.goods.entity.AttrEntity;
import com.hlju.onlineshop.goods.service.AttrAttrGroupRelationService;
import com.hlju.onlineshop.goods.service.AttrService;
import com.hlju.onlineshop.goods.service.CategoryService;
import com.hlju.onlineshop.goods.vo.AttrGroupWithAttrsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hlju.onlineshop.goods.entity.AttrGroupEntity;
import com.hlju.onlineshop.goods.service.AttrGroupService;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;

/**
 * 属性分组
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@RestController
@RequestMapping("/goods/attr-group")
public class AttrGroupController {

    private final AttrGroupService attrGroupService;
    private final CategoryService categoryService;
    private final AttrService attrService;
    private final AttrAttrGroupRelationService relationService;

    @Autowired
    AttrGroupController(AttrGroupService attrGroupService,
                        CategoryService categoryService,
                        AttrService attrService,
                        AttrAttrGroupRelationService relationService) {
        this.attrGroupService = attrGroupService;
        this.categoryService = categoryService;
        this.attrService = attrService;
        this.relationService = relationService;
    }

    @GetMapping("/attr/relation")
    public R getAttrRelation(@RequestParam("attrGroupId") Long attrGroupId) {
        List<AttrEntity> attrs = attrService.getAttrByAttrGroupId(attrGroupId);
        return R.ok().put("data", attrs);
    }

    @PostMapping("/attr/relation")
    public R addAttrRelation(@RequestBody List<AttrAttrGroupRelationDTO> relations) {
        relationService.saveBatch(relations);
        return R.ok();
    }

    @GetMapping("/attr/no-relation")
    public R noAttrRelation(@RequestParam("attrGroupId") Long attrGroupId,
                            @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoAttrRelations(params, attrGroupId);
        return R.ok().put("page", page);
    }

    // /${this.spu.categoryId}/withattr

    @GetMapping("/{categoryId}/with-attr")
    public R getAttrGroupWithAttrsByCategoryId(@PathVariable("categoryId") Long categoryId) {
        List<AttrGroupWithAttrsVO> data = attrGroupService.getAttrGroupWithAttrsByCategoryId(categoryId);
        return R.ok().put("data", data);
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{categoryId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable Long categoryId) {
        PageUtils page = attrGroupService.queryPageByCategoryId(params, categoryId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<Long> categoryPath = categoryService.getCategoryPathById(attrGroup.getCategoryId());
        attrGroup.setCategoryPath(categoryPath);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 通过属性id和属性分组id删除
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrAttrGroupRelationDTO> relationDtoList) {
        attrService.removeByRelation(relationDtoList);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
