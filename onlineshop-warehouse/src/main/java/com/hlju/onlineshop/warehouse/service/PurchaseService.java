package com.hlju.onlineshop.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.exception.UserInputException;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.warehouse.dto.PurchaseDoneDTO;
import com.hlju.onlineshop.warehouse.dto.PurchaseMergeDTO;
import com.hlju.onlineshop.warehouse.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未接受的采购单（未出发采购的采购单）
     * @param params 分页参数
     * @return page
     */
    PageUtils queryPageUnReceivePurchase(Map<String, Object> params);

    /**
     * 合并采购单
     * @param mergeDTO dto
     */
    void mergePurchase(PurchaseMergeDTO mergeDTO) throws UserInputException;

    /**
     * 领取采购单
     * @param purchaseIds 采购单ids
     */
    void receivePurchase(List<Long> purchaseIds);

    /**
     * 完成采购单
     * @param doneDTO dto
     */
    void donePurchase(PurchaseDoneDTO doneDTO);
}

