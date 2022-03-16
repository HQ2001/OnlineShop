package com.hlju.onlineshop.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品会员价格
 * 
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 10:19:10
 */
@Data
@TableName("sms_user_price")
public class UserPriceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * sku_id
	 */
	private Long skuId;
	/**
	 * 会员等级id
	 */
	private Long userLevelId;
	/**
	 * 会员等级名
	 */
	private String userLevelName;
	/**
	 * 会员对应价格
	 */
	private BigDecimal userPrice;
	/**
	 * 可否叠加其他优惠[0-不可叠加优惠，1-可叠加]
	 */
	private Integer addOther;

}
