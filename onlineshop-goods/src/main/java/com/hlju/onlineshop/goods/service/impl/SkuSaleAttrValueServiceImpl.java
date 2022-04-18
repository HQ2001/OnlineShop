package com.hlju.onlineshop.goods.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.hlju.onlineshop.goods.entity.SkuInfoEntity;
import com.hlju.onlineshop.goods.service.SkuInfoService;
import com.hlju.onlineshop.goods.vo.SkuInfoDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.SkuSaleAttrValueDao;
import com.hlju.onlineshop.goods.entity.SkuSaleAttrValueEntity;
import com.hlju.onlineshop.goods.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    private final SkuInfoService skuInfoService;

    @Autowired
    SkuSaleAttrValueServiceImpl(@Lazy SkuInfoService skuInfoService) {
        this.skuInfoService = skuInfoService;

    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoDetailVO.SaleAttrVO> getSaleAttrsBySpuId(Long spuId) {
        List<Long> skuIds = skuInfoService.getSkusBySpuId(spuId)
                .stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        // 将sku销售属性按照属性id进行分组
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = baseMapper.listBySkuIds(skuIds);
        // 这个list是为了保证属性的顺序
        List<Long> attrIds = skuSaleAttrValueEntities.stream()
                .map(SkuSaleAttrValueEntity::getAttrId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, List<SkuSaleAttrValueEntity>> attrIdMap = skuSaleAttrValueEntities.stream()
                .collect(Collectors.groupingBy(SkuSaleAttrValueEntity::getAttrId));
        List<SkuInfoDetailVO.SaleAttrVO> vos = new ArrayList<>();
        attrIds.forEach(attrId -> {
            List<SkuSaleAttrValueEntity> skuSaleAttrValues = attrIdMap.get(attrId);
            if (CollectionUtils.isEmpty(skuSaleAttrValues)) {
                return;
            }
            SkuInfoDetailVO.SaleAttrVO vo = new SkuInfoDetailVO.SaleAttrVO();
            vo.setAttrName(skuSaleAttrValues.get(0).getAttrName());
            // 在当前属性下，按照属性值分组，k - 属性值   v - 对应的skuId
            Map<String, List<Long>> saleAttrValueAndSkuIdsMap = skuSaleAttrValues.stream()
                    .collect(Collectors.groupingBy(
                            SkuSaleAttrValueEntity::getAttrValue,
                            Collectors.mapping(
                                    SkuSaleAttrValueEntity::getSkuId,
                                    Collectors.toList()
                            )
                    ));
            // 这个list是为了保证属性value的顺序
            List<String> valueList = skuSaleAttrValues.stream()
                    .map(SkuSaleAttrValueEntity::getAttrValue)
                    .distinct()
                    .collect(Collectors.toList());
            List<SkuInfoDetailVO.AttrValueWithSkuIdVO> attrValues = new ArrayList<>();
            valueList.forEach(value -> {
                List<Long> skuIdsForAttrValue = saleAttrValueAndSkuIdsMap.get(value);
                SkuInfoDetailVO.AttrValueWithSkuIdVO valueVO = new SkuInfoDetailVO.AttrValueWithSkuIdVO();
                valueVO.setAttrValue(value);
                valueVO.setSkuIds(skuIdsForAttrValue);
                attrValues.add(valueVO);
            });
            vo.setAttrValues(attrValues);
            vos.add(vo);
        });
        return vos;
    }

}