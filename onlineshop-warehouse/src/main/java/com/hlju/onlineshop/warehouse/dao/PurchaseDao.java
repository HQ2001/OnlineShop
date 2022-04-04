package com.hlju.onlineshop.warehouse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hlju.onlineshop.warehouse.entity.PurchaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {

    /**
     * 根据采购单ids更新status
     * @param purchaseIds 采购单ids
     * @param status status
     */
    void updateStatusByPurchaseIds(@Param("list") List<Long> purchaseIds, @Param("status") Integer status);
}
