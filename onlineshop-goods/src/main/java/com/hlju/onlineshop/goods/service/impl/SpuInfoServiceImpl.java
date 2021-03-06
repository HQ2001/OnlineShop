package com.hlju.onlineshop.goods.service.impl;

import com.hlju.common.to.SkuReductionTO;
import com.hlju.common.to.SpuBoundsTO;
import com.hlju.common.to.UserPriceTO;
import com.hlju.common.utils.R;
import com.hlju.onlineshop.goods.dao.*;
import com.hlju.onlineshop.goods.dto.*;
import com.hlju.onlineshop.goods.entity.*;
import com.hlju.onlineshop.goods.feign.CouponFeignService;
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

    private final SpuInfoDescDao spuInfoDescDao;
    private final SpuImagesService spuImagesService;
    private final AttrDao attrDao;
    private final GoodAttrValueService attrValueService;
    private final SkuInfoService skuInfoService;
    private final SkuImagesService skuImagesService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final CouponFeignService couponFeignService;

    @Autowired
    SpuInfoServiceImpl(SpuInfoDescDao spuInfoDescDao,
                       SpuImagesService spuImagesService,
                       AttrDao attrDao,
                       GoodAttrValueService attrValueService,
                       SkuInfoService skuInfoService,
                       SkuImagesService skuImagesService,
                       SkuSaleAttrValueService skuSaleAttrValueService,
                       CouponFeignService couponFeignService) {
        this.spuInfoDescDao = spuInfoDescDao;
        this.spuImagesService = spuImagesService;
        this.attrDao = attrDao;
        this.attrValueService = attrValueService;
        this.skuInfoService = skuInfoService;
        this.skuImagesService = skuImagesService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
        this.couponFeignService = couponFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    // TODO ???????????????????????????????????????
    @Transactional
    @Override
    public void saveSpuDetail(SpuSaveDTO spuSaveDto) {
        // ??????spu???????????? spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveDto, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();

        // ??????spu???????????? spu_info_desc
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuId);
        List<String> descriptions = spuSaveDto.getDescription();
        descEntity.setDescription(StringUtils.join(descriptions, ","));
        spuInfoDescDao.insert(descEntity);

        // ??????spu????????? spu_images
        spuImagesService.saveImagesWithSpuId(spuId, spuSaveDto.getImages());

        // ??????spu???????????? attr_value
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

        // spu???????????? spu_bounds
        BoundsDTO bounds = spuSaveDto.getBounds();
        SpuBoundsTO spuBoundsTo = new SpuBoundsTO();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuId);
        if (spuBoundsTo.getBuyBounds().compareTo(new BigDecimal("0")) > 0
                || spuBoundsTo.getGrowBounds().compareTo(new BigDecimal("0")) > 0) {
            R r = couponFeignService.saveSpuBounds(spuBoundsTo);
            if (r.getCode() != 0) {
                log.error("????????????spu??????????????????");
            }
        }

        // ??????spu?????????sku??????
        List<SkuDTO> skus = spuSaveDto.getSkus();
        // ???????????????????????????????????????sku????????????????????????????????????
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
            // ??????sku???????????? sku_info
            skuInfoService.save(skuInfoEntity);

            Long skuId = skuInfoEntity.getSkuId();
            // ????????????sku???????????????
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

            // ????????????sku????????????????????????
            List<SkuSaleAttrValueEntity> skuSaleAttrValues = skuDto.getAttr().stream()
                    .map(attr -> {
                        SkuSaleAttrValueEntity saleAttrValue = new SkuSaleAttrValueEntity();
                        BeanUtils.copyProperties(attr, saleAttrValue);
                        saleAttrValue.setSkuId(skuId);
                        return saleAttrValue;
                    }).collect(Collectors.toList());
            allSkuSaleAttrValues.addAll(skuSaleAttrValues);

            // sku????????????????????? sms -> sku_ladder | sku_full_reduction | user_price
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
                log.error("????????????spu??????????????????");
            }
        });

        // sku???????????? sku_images
        skuImagesService.saveBatch(allSkuImages);

        // sku???????????? sku_sale_attr_value
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

}