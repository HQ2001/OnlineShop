package com.hlju.onlineshop.search.service;

import com.hlju.onlineshop.search.dto.SearchParamDTO;
import com.hlju.onlineshop.search.vo.SearchResponseVO;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/14 10:41
 */
public interface OnlineShopSearchService {
    /**
     * 进行检索
     * @param param 检索的参数
     * @return 结果
     */
    SearchResponseVO search(SearchParamDTO param);
}
