package com.hlju.onlineshop.warehouse.dto;

import lombok.Data;

import java.util.List;

/**
 * 合并采购单dto
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/3 9:06
 */
@Data
public class PurchaseMergeDTO {

    /**
     * 采购单id
     */
    private Long purchaseId;

    /**
     * 合并项列表（采购需求id列表）
     */
    private List<Long> items;

}
