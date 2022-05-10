package com.hlju.onlineshop.goods.service.impl;

import com.hlju.onlineshop.goods.entity.SkuImagesEntity;
import com.hlju.onlineshop.goods.entity.SpuInfoEntity;
import com.hlju.onlineshop.goods.service.*;
import com.hlju.onlineshop.goods.vo.SkuInfoDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.goods.dao.SkuInfoDao;
import com.hlju.onlineshop.goods.entity.SkuInfoEntity;


@Slf4j
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    private final SkuImagesService skuImagesService;
    private final AttrGroupService attrGroupService;
    private final SkuSaleAttrValueService saleAttrValueService;
    private final SpuInfoService spuInfoService;
    private final ThreadPoolExecutor executor;

    @Autowired
    SkuInfoServiceImpl(SkuImagesService skuImagesService,
                       AttrGroupService attrGroupService,
                       SkuSaleAttrValueService saleAttrValueService,
                       @Lazy SpuInfoService spuInfoService,
                       ThreadPoolExecutor executor) {
        this.skuImagesService = skuImagesService;
        this.attrGroupService = attrGroupService;
        this.saleAttrValueService = saleAttrValueService;
        this.spuInfoService = spuInfoService;
        this.executor = executor;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and((wrapper) -> wrapper.eq("sku_id", key).or().like("sku_name", key));
        }

        setQueryCondition(queryWrapper, params);

        String min = (String) params.get("min");
        if (StringUtils.isNotEmpty(min)) {
            queryWrapper.ge("price", min);
        }
        String max = (String) params.get("max");
        if (StringUtils.isNotEmpty(max) && Long.parseLong(max) > 0L) {
            queryWrapper.le("price", max);
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    public void setQueryCondition(QueryWrapper<?> queryWrapper, Map<String, Object> params) {
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId) && Long.parseLong(brandId) > 0L) {
            queryWrapper.eq("brand_id", brandId);
        }
        String categoryId = (String) params.get("categoryId");
        if (StringUtils.isNotEmpty(categoryId) && Long.parseLong(categoryId) > 0L) {
            queryWrapper.eq("category_id", categoryId);
        }
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        return baseMapper.listBySpuId(spuId);
    }

    @Override
    public SkuInfoDetailVO infoDetail(Long skuId) {
        SkuInfoDetailVO vo = new SkuInfoDetailVO();
        CountDownLatch countDownLatch = new CountDownLatch(5);
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            // sku基本信息 gms_sku_info
            SkuInfoEntity skuInfoEntity = baseMapper.selectById(skuId);
            vo.setSkuInfoEntity(skuInfoEntity);
            countDownLatch.countDown();
            log.debug("1.sku基本信息查询成功。。");
            return skuInfoEntity;
        }, executor);

        infoFuture.thenAcceptAsync(res -> {
            // spu介绍（图片）
            SpuInfoEntity spuInfoEntity = spuInfoService.getById(res.getSpuId());
            vo.setSpuDescriptionImages(spuInfoEntity.getDescriptionImages());
            countDownLatch.countDown();
            log.debug("2.spu介绍（图片）查询成功。。");
        }, executor);

        infoFuture.thenAcceptAsync(res -> {
            // 获取spu销售属性组合(其他sku)
            List<SkuInfoDetailVO.SaleAttrVO> saleAttrs = saleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            vo.setSaleAttrs(saleAttrs);
            countDownLatch.countDown();
            log.debug("3.spu销售属性组合(其他sku)查询成功。。");
        }, executor);

        infoFuture.thenAcceptAsync(res -> {
            // spu的规格参数信息(基础属性，属性组-属性)
            List<SkuInfoDetailVO.AttrGroupVO> attrGroups = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId());
            log.debug("attrGroups {}", attrGroups);
            vo.setAttrGroups(attrGroups);
            log.debug("4.spu的规格参数信息查询成功。。");
            countDownLatch.countDown();
        }, executor);

        CompletableFuture.runAsync(() -> {
            // sku图片信息 gms_sku_images
            List<SkuImagesEntity> skuImages = skuImagesService.listBySkuId(skuId);
            vo.setSkuImages(skuImages);
            countDownLatch.countDown();
            log.debug("5.sku图片信息查询成功。。");
        }, executor);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return vo;
    }

}