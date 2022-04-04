package com.hlju.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * sku 满减信息
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/1 14:58
 */
@Data
public class SkuReductionTO {
    private Long skuId;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer priceStatus;
    private List<UserPriceTO> userPrice;
}
