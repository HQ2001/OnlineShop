package com.hlju.onlineshop.goods.vo;

import com.hlju.onlineshop.goods.entity.AttrEntity;
import com.hlju.onlineshop.goods.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/3/28 13:01
 */
@Data
public class AttrGroupWithAttrsVO extends AttrGroupEntity {

    List<AttrEntity> attrs;

}
