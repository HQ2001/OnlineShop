package com.hlju.onlineshop.order.vo;

import lombok.Data;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/26 9:46
 */
@Data
public class UserAddressVO {


    /**
     * id
     */
    private Long id;
    /**
     * user_id
     */
    private Long userId;
    /**
     * 收货人姓名
     */
    private String name;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮政编码
     */
    private String postCode;
    /**
     * 省份/直辖市
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区
     */
    private String region;
    /**
     * 详细地址(街道)
     */
    private String detailAddress;
    /**
     * 省市区代码
     */
    private String areaCode;
    /**
     * 是否默认
     */
    private Integer defaultStatus;

}
