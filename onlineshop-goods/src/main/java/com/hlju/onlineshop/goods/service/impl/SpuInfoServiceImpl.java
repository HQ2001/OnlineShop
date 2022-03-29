package com.hlju.onlineshop.goods.service.impl;

import com.hlju.onlineshop.goods.dao.*;
import com.hlju.onlineshop.goods.dto.BaseAttrDTO;
import com.hlju.onlineshop.goods.dto.ImageDTO;
import com.hlju.onlineshop.goods.dto.SkuDTO;
import com.hlju.onlineshop.goods.dto.SpuSaveDTO;
import com.hlju.onlineshop.goods.entity.*;
import com.hlju.onlineshop.goods.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    SpuInfoServiceImpl(SpuInfoDescDao spuInfoDescDao,
                       SpuImagesService spuImagesService,
                       AttrDao attrDao,
                       GoodAttrValueService attrValueService,
                       SkuInfoService skuInfoService,
                       SkuImagesService skuImagesService,
                       SkuSaleAttrValueService skuSaleAttrValueService) {
        this.spuInfoDescDao = spuInfoDescDao;
        this.spuImagesService = spuImagesService;
        this.attrDao = attrDao;
        this.attrValueService = attrValueService;
        this.skuInfoService = skuInfoService;
        this.skuImagesService = skuImagesService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuDetail(SpuSaveDTO spuSaveDto) {
        // 保存spu基本信息 spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveDto, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();

        // 保存spu描述图片 spu_info_desc
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuId);
        List<String> descriptions = spuSaveDto.getDescription();
        descEntity.setDescription(StringUtils.join(descriptions, ","));
        spuInfoDescDao.insert(descEntity);

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
            skuInfoEntity.setSkuId(spuInfoEntity.getId());
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
        });

        // sku图片信息 sku_images
        skuImagesService.saveBatch(allSkuImages);

        // sku销售属性 sku_sale_attr_value
        skuSaleAttrValueService.saveBatch(allSkuSaleAttrValues);

        // sku优惠、满减信息 sms -> sku_ladder | sku_full_reduction | user_price

    }

}