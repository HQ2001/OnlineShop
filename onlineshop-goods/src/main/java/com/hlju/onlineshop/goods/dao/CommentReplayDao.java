package com.hlju.onlineshop.goods.dao;

import com.hlju.onlineshop.goods.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022-03-14 11:13:55
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {

}
