package com.hlju.onlineshop.goods.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/10 16:24
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Catalog2VO {
    // 一级父分类id
    private String catalog1Id;
    private String id;
    private String name;
    private List<Catalog3VO> catalog3List;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Catalog3VO {
        private String catalog2Id;
        private String id;
        private String name;
    }
}
