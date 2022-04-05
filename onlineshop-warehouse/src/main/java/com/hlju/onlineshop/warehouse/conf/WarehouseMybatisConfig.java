package com.hlju.onlineshop.warehouse.conf;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/3 18:51
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.hlju.onlineshop.warehouse.dao")
public class WarehouseMybatisConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 请求大于最大页，true 跳回首页；false 继续请求
        paginationInterceptor.setOverflow(true);
        paginationInterceptor.setLimit(1000);
        return paginationInterceptor;
    }
}
