package com.hlju.onlineshop.warehouse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hlju.onlineshop.warehouse.entity.PurchaseDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购单详细信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-13 22:50:18
 */
@Mapper
public interface PurchaseDetailDao extends BaseMapper<PurchaseDetailEntity> {

    /**
     * 根据采购单ids查询所有采购项
     * @param purchaseIds 采购单ids
     * @return 实体类list
     */
    List<PurchaseDetailEntity> listByPurchaseIds(@Param("list") List<Long> purchaseIds);

    /**
     * 根据采购单ids更新status
     * @param purchaseIds 采购单ids
     * @param status status
     */
    void updateStatusByPurchaseIds(@Param("list") List<Long> purchaseIds, @Param("status") Integer status);
}
