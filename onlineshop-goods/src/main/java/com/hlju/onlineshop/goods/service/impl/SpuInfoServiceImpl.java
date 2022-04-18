package com.hlju.onlineshop.goods.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.hlju.common.enums.goods.AttrSearchTypeEnum;
import com.hlju.common.enums.goods.GoodStatusEnum;
import com.hlju.common.enums.goods.PurchaseStatusEnum;
import com.hlju.common.to.SkuReductionTO;
import com.hlju.common.to.SpuBoundsTO;
import com.hlju.common.to.UserPriceTO;
import com.hlju.common.to.es.SkuEsModel;
import com.hlju.common.utils.R;
import com.hlju.onlineshop.goods.dao.*;
import com.hlju.onlineshop.goods.dto.*;
import com.hlju.onlineshop.goods.entity.*;
import com.hlju.onlineshop.goods.feign.CouponFeignService;
import com.hlju.onlineshop.goods.feign.SearchFeignService;
import com.hlju.onlineshop.goods.feign.WarehouseFeignService;
import com.hlju.onlineshop.goods.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    private final SpuImagesService spuImagesService;
    private final AttrDao attrDao;
    private final GoodAttrValueService attrValueService;
    private final SkuInfoService skuInfoService;
    private final SkuImagesService skuImagesService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final CouponFeignService couponFeignService;
    private final WarehouseFeignService warehouseFeignService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final SearchFeignService searchFeignService;

    @Autowired
    SpuInfoServiceImpl(SpuImagesService spuImagesService,
                       AttrDao attrDao,
                       GoodAttrValueService attrValueService,
                       SkuInfoService skuInfoService,
                       SkuImagesService skuImagesService,
                       SkuSaleAttrValueService skuSaleAttrValueService,
                       CouponFeignService couponFeignService,
                       WarehouseFeignService warehouseFeignService,
                       CategoryService categoryService,
                       BrandService brandService,
                       SearchFeignService searchFeignService) {
        this.spuImagesService = spuImagesService;
        this.attrDao = attrDao;
        this.attrValueService = attrValueService;
        this.skuInfoService = skuInfoService;
        this.skuImagesService = skuImagesService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
        this.couponFeignService = couponFeignService;
        this.warehouseFeignService = warehouseFeignService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.searchFeignService = searchFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    // TODO 完善不成功时的事务回滚逻辑
    @Transactional
    @Override
    public void saveSpuDetail(SpuSaveDTO spuSaveDto) {
        // 保存spu基本信息 spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveDto, spuInfoEntity);
        List<String> descriptions = spuSaveDto.getDescription();
        spuInfoEntity.setDescriptionImages(StringUtils.join(descriptions, ","));
        this.save(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();

        // 保存spu图片集 spu_images
        spuImagesService.saveImagesWithSpuId(spuId, spuSaveDto.getImages());

        // 保存spu规格参数 attr_value
        List<BaseAttrDTO> baseAttrs = spuSaveDto.getBaseAttrs();
        List<Long> attrIds = baseAttrs.stream().map(BaseAttrDTO::getAttrId).collect(Collectors.toList());
        Map<Long, String> attrIdAndNameMap = attrDao.selectBatchIds(attrIds).stream()
                .collect(Collectors.toMap(AttrEntity::getAttrId, AttrEntity::getAttrName));
        List<GoodAttrValueEntity> attrValueEntities = baseAttrs.stream()
                .map(baseAttr -> {
                    GoodAttrValueEntity attrValueEntity = new GoodAttrValueEntity();
                    attrValueEntity.setSpuId(spuId);
                    attrValueEntity.setAttrId(baseAttr.getAttrId());
                    attrValueEntity.setAttrName(attrIdAndNameMap.get(baseAttr.getAttrId()));
                    attrValueEntity.setAttrValue(baseAttr.getAttrValues());
                    attrValueEntity.setQuickShow(baseAttr.getShowDesc());
                    return attrValueEntity;
                }).collect(Collectors.toList());
        attrValueService.saveBatch(attrValueEntities);

        // spu积分信息 spu_bounds
        BoundsDTO bounds = spuSaveDto.getBounds();
        SpuBoundsTO spuBoundsTo = new SpuBoundsTO();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuId);
        if (spuBoundsTo.getBuyBounds().compareTo(new BigDecimal("0")) > 0
                || spuBoundsTo.getGrowBounds().compareTo(new BigDecimal("0")) > 0) {
            R r = couponFeignService.saveSpuBounds(spuBoundsTo);
            if (r.getCode() != 0) {
                log.error("远程保存spu积分信息失败");
            }
        }

        // 保存spu对应的sku信息
        List<SkuDTO> skus = spuSaveDto.getSkus();
        // 要保存的其他信息，遍历每个sku时进行手机，最后批量保存
        List<SkuImagesEntity> allSkuImages = new ArrayList<>();
        List<SkuSaleAttrValueEntity> allSkuSaleAttrValues = new ArrayList<>();

        skus.forEach(skuDto -> {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(skuDto, skuInfoEntity);
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setCategoryId(spuInfoEntity.getCategoryId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            String defaultImg = "";
            for (ImageDTO image : skuDto.getImages()) {
                if (image.getDefaultImg() == 1) {
                    defaultImg = image.getImgUrl();
                }
            }
            skuInfoEntity.setSkuDefaultImg(defaultImg);
            // 保存sku基本信息 sku_info
            skuInfoService.save(skuInfoEntity);

            Long skuId = skuInfoEntity.getSkuId();
            // 构建当前sku的所有图片
            List<SkuImagesEntity> skuImages = skuDto.getImages().stream()
                    .filter(imageDTO -> StringUtils.isNotEmpty(imageDTO.getImgUrl()))
                    .map(imageDto -> {
                        SkuImagesEntity skuImage = new SkuImagesEntity();
                        skuImage.setSkuId(skuId);
                        skuImage.setDefaultImg(imageDto.getDefaultImg());
                        skuImage.setImgUrl(imageDto.getImgUrl());
                        return skuImage;
                    }).collect(Collectors.toList());
            allSkuImages.addAll(skuImages);

            // 构建当前sku的所有销售属性值
            List<SkuSaleAttrValueEntity> skuSaleAttrValues = skuDto.getAttr().stream()
                    .map(attr -> {
                        SkuSaleAttrValueEntity saleAttrValue = new SkuSaleAttrValueEntity();
                        BeanUtils.copyProperties(attr, saleAttrValue);
                        saleAttrValue.setSkuId(skuId);
                        return saleAttrValue;
                    }).collect(Collectors.toList());
            allSkuSaleAttrValues.addAll(skuSaleAttrValues);

            // sku优惠、满减信息 sms -> sku_ladder | sku_full_reduction | user_price
            SkuReductionTO skuReductionTo = new SkuReductionTO();
            BeanUtils.copyProperties(skuDto, skuReductionTo);
            List<UserPriceTO> userPriceTOList = skuDto.getUserPrice().stream()
                    .map(userPriceDto -> {
                        UserPriceTO userPriceTO = new UserPriceTO();
                        BeanUtils.copyProperties(userPriceDto, userPriceTO);
                        return userPriceTO;
                    }).collect(Collectors.toList());
            skuReductionTo.setUserPrice(userPriceTOList);
            skuReductionTo.setSkuId(skuId);
            R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
            if (r1.getCode() != 0) {
                log.error("远程保存spu优惠信息失败");
            }
        });

        // sku图片信息 sku_images
        skuImagesService.saveBatch(allSkuImages);

        // sku销售属性 sku_sale_attr_value
        skuSaleAttrValueService.saveBatch(allSkuSaleAttrValues);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and((wrapper) -> wrapper.eq("id", key).or().like("spu_name", key));
        }
        String publishStatus = (String) params.get("status");
        if (StringUtils.isNotEmpty(publishStatus)) {
            queryWrapper.eq("publish_status", publishStatus);
        }

        skuInfoService.setQueryCondition(queryWrapper, params);

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        // 查询当前sku的所以可以被用来检索的规格属性
        List<GoodAttrValueEntity> baseAttrs = attrValueService.baseAttrListForSpu(spuId);
        List<Long> allAttrIds = baseAttrs.stream().map(GoodAttrValueEntity::getAttrId).collect(Collectors.toList());
        // 查询出可检索且在当前spuId下的所有属性
        Set<Long> attrIdSet = attrDao.listBySearchTypeAndAttrIds(AttrSearchTypeEnum.SEARCH.getCode(), allAttrIds)
                .stream()
                .map(AttrEntity::getAttrId)
                .collect(Collectors.toSet());
        List<SkuEsModel.Attr> attrsList = baseAttrs.stream()
                .filter(item -> attrIdSet.contains(item.getAttrId()))
                .map(item -> {
                    SkuEsModel.Attr attr = new SkuEsModel.Attr();
                    BeanUtils.copyProperties(item, attr);
                    return attr;
                }).collect(Collectors.toList());

        List<SkuEsModel> upGoods = Lists.newArrayList();
        // sku基本信息
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIds = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        // 远程调用获取是否有库存
        Map<Long, Boolean> hasStockMap = null;
        try {
            hasStockMap = warehouseFeignService.getSkusHasStock(skuIds).getData(new TypeReference<Map<Long, Boolean>>(){});
            System.out.println(hasStockMap.keySet());
        } catch (Exception e) {
            log.error("库存服务异常{}", e);
        }

        List<Long> categoryIds = skus.stream()
                .map(SkuInfoEntity::getCategoryId)
                .collect(Collectors.toList());
        List<Long> brandIds = skus.stream()
                .map(SkuInfoEntity::getBrandId)
                .collect(Collectors.toList());
        Map<Long, String> categoryIdAndNameMap = categoryService.listByIds(categoryIds).stream()
                .collect(Collectors.toMap(CategoryEntity::getCatId, CategoryEntity::getName));
        Map<Long, BrandEntity> brandIdMap = brandService.listByIds(brandIds).stream()
                .collect(Collectors.toMap(BrandEntity::getBrandId, item -> item));
        Map<Long, Boolean> finalHasStockMap = hasStockMap;
        skus.forEach(skuInfoEntity -> {
            // 封装成es中的数据
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity, skuEsModel);
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());

            if (Objects.nonNull(finalHasStockMap)) {
                Boolean hasStock = finalHasStockMap.get(skuEsModel.getSkuId());
                skuEsModel.setHasStock(hasStock);
            } else {
                // 远程调用失败默认有库存
                skuEsModel.setHasStock(true);
            }
            skuEsModel.setHotScore(0L);
            skuEsModel.setCategoryName(categoryIdAndNameMap.get(skuInfoEntity.getCategoryId()));
            BrandEntity brandEntity = brandIdMap.get(skuInfoEntity.getBrandId());
            if (Objects.nonNull(brandEntity)) {
                skuEsModel.setBrandName(brandEntity.getName());
                skuEsModel.setBrandImg(brandEntity.getLogo());
            }
            skuEsModel.setAttrs(attrsList);
            upGoods.add(skuEsModel);
        });

        R r = searchFeignService.goodsUp(upGoods);
        if (r.getCode() == 0) {
            baseMapper.updateSpuStatus(spuId, GoodStatusEnum.UP.getCode());
        } else {
            // 远程调用失败
            // TODO 重复调用，幂等性问题
        }
    }

}