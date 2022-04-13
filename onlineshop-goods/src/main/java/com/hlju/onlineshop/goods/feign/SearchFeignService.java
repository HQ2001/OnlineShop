package com.hlju.onlineshop.goods.feign;

import com.hlju.common.to.es.SkuEsModel;
import com.hlju.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/10 9:48
 */
@FeignClient("onlineshop-search")
public interface SearchFeignService {
    @PostMapping("/es/save/goods")
    R goodsUp(@RequestBody List<SkuEsModel> skuEsModels);
}
