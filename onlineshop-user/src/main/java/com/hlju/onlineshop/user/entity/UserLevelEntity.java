package com.hlju.onlineshop.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 会员等级
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 09:58:06
 */
@Data
@TableName("ums_user_level")
public class UserLevelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 等级名称
     */
    private String name;
    /**
     * 等级需要的成长值
     */
    private Integer growthPoint;
    /**
     * 是否为默认等级[0->不是；1->是]
     */
    private Integer defaultStatus;
    /**
     * 免运费标准
     */
    private BigDecimal freeFreightPoint;
    /**
     * 每次评价获取的成长值
     */
    private Integer commentGrowthPoint;
    /**
     * 是否有免邮特权
     */
    private Integer hasFreeFreight;
    /**
     * 是否有会员价格特权
     */
    private Integer hasUserPrice;
    /**
     * 是否有生日特权
     */
    private Integer hasBirthday;
    /**
     * 备注
     */
    private String note;

}
