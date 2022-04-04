package com.hlju.onlineshop.coupon.service.impl;

import com.hlju.common.to.SkuReductionTO;
import com.hlju.common.to.UserPriceTO;
import com.hlju.onlineshop.coupon.entity.SkuLadderEntity;
import com.hlju.onlineshop.coupon.entity.UserPriceEntity;
import com.hlju.onlineshop.coupon.service.SkuLadderService;
import com.hlju.onlineshop.coupon.service.UserPriceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.coupon.dao.SkuFullReductionDao;
import com.hlju.onlineshop.coupon.entity.SkuFullReductionEntity;
import com.hlju.onlineshop.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    private final SkuLadderService skuLadderService;
    private final UserPriceService userPriceService;

    @Autowired
    SkuFullReductionServiceImpl(SkuLadderService skuLadderService,
                                UserPriceService userPriceService) {
        this.skuLadderService = skuLadderService;
        this.userPriceService = userPriceService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTO skuReductionTO) {
        // sku优惠、满减信息 sms -> sku_ladder | sku_full_reduction | user_price
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTO, skuLadderEntity);
        skuLadderEntity.setAddOther(skuReductionTO.getCountStatus());
        if (skuLadderEntity.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }

        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTO, skuFullReductionEntity);
        if (skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0")) > 0) {
            this.save(skuFullReductionEntity);
        }

        List<UserPriceTO> userPrice = skuReductionTO.getUserPrice();
        List<UserPriceEntity> userPriceEntities = userPrice.stream()
                .filter(userPriceTO -> userPriceTO.getPrice().compareTo(new BigDecimal("0")) > 0)
                .map(userPriceTO -> {
                    UserPriceEntity userPriceEntity = new UserPriceEntity();
                    userPriceEntity.setSkuId(skuReductionTO.getSkuId());
                    userPriceEntity.setUserLevelId(userPriceTO.getId());
                    userPriceEntity.setUserLevelName(userPriceTO.getName());
                    userPriceEntity.setUserPrice(userPriceTO.getPrice());
                    // 默认叠加其他优惠
                    userPriceEntity.setAddOther(1);
                    return userPriceEntity;
                }).collect(Collectors.toList());
        this.userPriceService.saveBatch(userPriceEntities);
    }

}