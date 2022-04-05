package com.hlju.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * spu积分信息to
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/1 14:44
 */
@Data
public class SpuBoundsTO {

    private Long spuId;

    /**
     * 成长积分
     */
    private BigDecimal growBounds;
    /**
     * 购物积分
     */
    private BigDecimal buyBounds;

}
