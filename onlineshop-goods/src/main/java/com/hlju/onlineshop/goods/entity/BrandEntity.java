package com.hlju.onlineshop.goods.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.hlju.common.valid.AddGroup;
import com.hlju.common.valid.ListValue;
import com.hlju.common.valid.UpdateGroup;
import com.hlju.common.valid.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@Data
@TableName("gms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @Null(message = "新增不可指定id", groups = {AddGroup.class})
    @NotNull(message = "修改需要指定品牌id", groups = {UpdateGroup.class})
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotBlank(message = "品牌logo地址不能为空", groups = {AddGroup.class})
    @URL(message = "品牌logo地址格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    private String logo;
    /**
     * 介绍
     */
    @NotBlank(message = "介绍不能为空", groups = {AddGroup.class})
    private String description;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(message = "显示状态不能为空", groups = {AddGroup.class, UpdateStatusGroup.class})
    @ListValue(values = {0, 1}, groups = {AddGroup.class, UpdateStatusGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotBlank(message = "检索首字母不可为空", groups = {AddGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$",
            message = "检索首字母必须为A-Z或者a-z之间的一个字母",
            groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(message = "排序不可为空", groups = {AddGroup.class})
    @Min(value = 0, message = "排序必须是一个大于等于0的整数", groups = {AddGroup.class, UpdateGroup.class})
    private Integer sort;

}
