package com.hlju.onlineshop.goods.vo;

import com.hlju.onlineshop.goods.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/3/25 20:00
 */
@Data
public class AttrVO extends AttrEntity {
    private static final long serialVersionUID = -7813786334205414209L;

    private String categoryName;

    private String groupName;

    private List<Long> categoryPath;

    private Long attrGroupId;

}
