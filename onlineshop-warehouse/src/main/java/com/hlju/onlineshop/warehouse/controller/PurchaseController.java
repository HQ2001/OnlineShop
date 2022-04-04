package com.hlju.onlineshop.warehouse.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hlju.common.exception.UserInputException;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.R;
import com.hlju.onlineshop.warehouse.dto.PurchaseDoneDTO;
import com.hlju.onlineshop.warehouse.dto.PurchaseMergeDTO;
import com.hlju.onlineshop.warehouse.entity.PurchaseEntity;
import com.hlju.onlineshop.warehouse.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 采购信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
@RestController
@RequestMapping("/warehouse/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchase.setUpdateTime(new Date());
        purchase.setCreateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询未接受的采购单（未出发采购的采购单）
     */
    @GetMapping("un-receive/list")
    public R unReceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnReceivePurchase(params);

        return R.ok().put("page", page);
    }

    /**
     * 合并采购单
     */
    @PostMapping("/merge")
    public R mergePurchase(@RequestBody PurchaseMergeDTO mergeDTO) throws UserInputException {
        purchaseService.mergePurchase(mergeDTO);

        return R.ok();
    }

    /**
     * 领取采购单
     */
    @PostMapping("/receive")
    public R receivePurchase(@RequestBody List<Long> purchaseIds) {
        purchaseService.receivePurchase(purchaseIds);

        return R.ok();
    }

    /**
     * 完成采购单
     */
    @PostMapping("/done")
    public R donePurchase(@RequestBody PurchaseDoneDTO doneDTO) {
        purchaseService.donePurchase(doneDTO);

        return R.ok();
    }
}
