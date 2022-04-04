package com.hlju.onlineshop.warehouse.dto;

import lombok.Data;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/3 17:21
 */
@Data
public class PurchaseDetailDoneDTO {

    /**
     * 采购项id
     */
    private Long detailId;

    /**
     * 采购项状态
     */
    private Integer status;

    /**
     * 异常原因
     */
    private String reason;

}
