package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.entity.SkuSaleAttrValueEntity;
import com.hlju.onlineshop.goods.vo.SkuInfoDetailVO;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据spuId查询出当前商品的所有sku
     * @param spuId spuId
     * @return vo
     */
    List<SkuInfoDetailVO.SaleAttrVO> getSaleAttrsBySpuId(Long spuId);

    /**
     * 根据skuId查询出其相关的销售属性list
     *  eg：[颜色：白色, 版本：8+256]
     * @param skuId skuId
     * @return 属性list
     */
    List<String> getSkuSaleAttrValues(Long skuId);
}

