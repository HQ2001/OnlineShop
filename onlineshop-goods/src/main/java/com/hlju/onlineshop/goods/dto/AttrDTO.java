package com.hlju.onlineshop.goods.dto;

import com.hlju.onlineshop.goods.entity.AttrEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/3/25 19:29
 */
@Data
public class AttrDTO extends AttrEntity {
    private static final long serialVersionUID = -5835568560915103799L;

    private Long attrGroupId;

}
