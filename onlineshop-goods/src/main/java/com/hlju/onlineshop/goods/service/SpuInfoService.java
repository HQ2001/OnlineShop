package com.hlju.onlineshop.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlju.common.utils.PageUtils;
import com.hlju.onlineshop.goods.dto.SpuSaveDTO;
import com.hlju.onlineshop.goods.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存spu详细信息
     * @param spuSaveDto dto
     */
    void saveSpuDetail(SpuSaveDTO spuSaveDto);

}

