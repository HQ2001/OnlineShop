package com.hlju.onlineshop.warehouse.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/3 17:20
 */
@Data
public class PurchaseDoneDTO {
    @NotNull
    private Long purchaseId;

    private List<PurchaseDetailDoneDTO> detailDoneDTOS;
}
