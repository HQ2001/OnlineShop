package com.hlju.onlineshop.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hlju.common.enums.goods.PurchaseDetailStatusEnum;
import com.hlju.common.enums.goods.PurchaseStatusEnum;
import com.hlju.common.exception.UserInputException;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;
import com.hlju.onlineshop.warehouse.dao.PurchaseDao;
import com.hlju.onlineshop.warehouse.dao.PurchaseDetailDao;
import com.hlju.onlineshop.warehouse.dto.PurchaseDetailDoneDTO;
import com.hlju.onlineshop.warehouse.dto.PurchaseDoneDTO;
import com.hlju.onlineshop.warehouse.dto.PurchaseMergeDTO;
import com.hlju.onlineshop.warehouse.entity.PurchaseDetailEntity;
import com.hlju.onlineshop.warehouse.entity.PurchaseEntity;
import com.hlju.onlineshop.warehouse.entity.WarehouseSkuEntity;
import com.hlju.onlineshop.warehouse.service.PurchaseDetailService;
import com.hlju.onlineshop.warehouse.service.PurchaseService;
import com.hlju.onlineshop.warehouse.service.WarehouseSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    private final PurchaseDetailService detailService;
    private final PurchaseDetailDao detailDao;
    private final WarehouseSkuService warehouseSkuService;

    @Autowired
    PurchaseServiceImpl(PurchaseDetailService detailService,
                        PurchaseDetailDao detailDao,
                        WarehouseSkuService warehouseSkuService) {
        this.detailService = detailService;
        this.detailDao = detailDao;
        this.warehouseSkuService = warehouseSkuService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnReceivePurchase(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", PurchaseStatusEnum.CREATED.getCode())
                .or().eq("status", PurchaseStatusEnum.ASSIGNED.getCode());
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(PurchaseMergeDTO mergeDTO) throws UserInputException {
        Long purchaseId = mergeDTO.getPurchaseId();
        List<Long> purchaseDetailIds = mergeDTO.getItems();
        // 判断采购项均为新建
        List<PurchaseDetailEntity> purchaseEntities = detailDao.selectBatchIds(purchaseDetailIds);
        for (PurchaseDetailEntity detailEntity : purchaseEntities) {
            if (!Objects.equals(PurchaseDetailStatusEnum.CREATED.getCode(), detailEntity.getStatus())) {
                throw new UserInputException("采购项不可更改!");
            }
        }

        // 新生成采购单为新建状态
        if (Objects.isNull(purchaseId)) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setPriority(1);
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        } else {
            PurchaseEntity purchaseEntity = baseMapper.selectById(purchaseId);
            Integer status = purchaseEntity.getStatus();
            // 如果采购单状态不为新建或已分配，不可改变采购项所属的采购单
            if (!Objects.equals(PurchaseStatusEnum.CREATED.getCode(), status)
                    && !Objects.equals(PurchaseStatusEnum.ASSIGNED.getCode(), status)) {
                return;
            }
        }

        // 采购项设置为已分配
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailIds.stream().map(item -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());
        detailService.updateBatchById(purchaseDetailEntities);
    }

    @Override
    public void receivePurchase(List<Long> purchaseIds) {

        // 更新采购单状态
        baseMapper.updateStatusByPurchaseIds(purchaseIds, PurchaseStatusEnum.RECEIVED.getCode());

        // 更新采购项的状态
        detailDao.updateStatusByPurchaseIds(purchaseIds, PurchaseDetailStatusEnum.BUYING.getCode());
    }

    @Transactional
    @Override
    public void donePurchase(PurchaseDoneDTO doneDTO) {

        List<PurchaseDetailEntity> detailEntities = Lists.newArrayList();
        List<Long> doneIds = Lists.newArrayList();

        Integer purchaseStatus = PurchaseStatusEnum.DONE.getCode();
        for (PurchaseDetailDoneDTO detailDoneDTO : doneDTO.getDetailDoneDTOS()) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (Objects.equals(PurchaseStatusEnum.HAS_EXCEPTION.getCode(), detailDoneDTO.getStatus())) {
                purchaseStatus = detailDoneDTO.getStatus();
                detailEntity.setStatus(detailDoneDTO.getStatus());
            } else {
                detailEntity.setStatus(PurchaseDetailStatusEnum.DONE.getCode());
                doneIds.add(detailDoneDTO.getDetailId());
            }
            detailEntity.setId(detailDoneDTO.getDetailId());
            detailEntities.add(detailEntity);
        }

        // 改变采购单状态
        baseMapper.updateStatusByPurchaseIds(
                Collections.singletonList(doneDTO.getPurchaseId()),
                purchaseStatus
        );

        // 改变采购项状态
        detailService.updateBatchById(detailEntities);

        // 将商品进行入库
        List<PurchaseDetailEntity> doneDetails = detailDao.selectBatchIds(doneIds);
        List<WarehouseSkuEntity> warehouseSkuEntities = doneDetails.stream()
                .map(doneDetail -> {
                    WarehouseSkuEntity warehouseSkuEntity = new WarehouseSkuEntity();
                    warehouseSkuEntity.setWarehouseId(doneDetail.getWarehouseId());
                    warehouseSkuEntity.setSkuId(doneDetail.getSkuId());
                    warehouseSkuEntity.setStock(doneDetail.getSkuNum());
                    warehouseSkuEntity.setStockLocked(0);
                    return warehouseSkuEntity;
                }).collect(Collectors.toList());

        warehouseSkuService.addStocks(warehouseSkuEntities);
    }

}