package com.hlju.onlineshop.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.hlju.common.vo.UserResponseVO;
import com.hlju.onlineshop.order.feign.CartFeignService;
import com.hlju.onlineshop.order.feign.UserFeignService;
import com.hlju.onlineshop.order.interceptor.LoginUserInterceptor;
import com.hlju.onlineshop.order.vo.OrderConfirmVO;
import com.hlju.onlineshop.order.vo.OrderItemVO;
import com.hlju.onlineshop.order.vo.UserAddressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlju.common.utils.PageUtils;
import com.hlju.common.utils.Query;

import com.hlju.onlineshop.order.dao.OrderDao;
import com.hlju.onlineshop.order.entity.OrderEntity;
import com.hlju.onlineshop.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private final UserFeignService userFeignService;
    private final CartFeignService cartFeignService;
    private final ThreadPoolExecutor executor;

    @Autowired
    OrderServiceImpl(UserFeignService userFeignService,
                     CartFeignService cartFeignService,
                     ThreadPoolExecutor executor) {
        this.userFeignService = userFeignService;
        this.cartFeignService = cartFeignService;
        this.executor = executor;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVO getConfirmOrder() throws InterruptedException {
        UserResponseVO loginUser = LoginUserInterceptor.loginUser.get();

        CountDownLatch countDownLatch = new CountDownLatch(2);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        OrderConfirmVO vo = new OrderConfirmVO();
        CompletableFuture.runAsync(() -> {
            // 解决异步情况下上下文丢失的问题
            RequestContextHolder.setRequestAttributes(requestAttributes);
            // 设置地址
            List<UserAddressVO> addresses = userFeignService.getAddressesByUserId(loginUser.getId())
                    .getData("addresses", new TypeReference<List<UserAddressVO>>() {
                    });
            vo.setAddresses(addresses);
            countDownLatch.countDown();
        }, executor);

        CompletableFuture.runAsync(() -> {
            // 解决异步情况下上下文丢失的问题
            RequestContextHolder.setRequestAttributes(requestAttributes);
            // 设置订单项（购物车中选中的项）
            List<OrderItemVO> orderItems = cartFeignService.getUpToDateCheckedCartItems(loginUser.getId());
            vo.setItems(orderItems);
        }, executor).thenRunAsync(() -> {
            List<OrderItemVO> items = vo.getItems();
            List<Long> skuIds = items.stream().map(OrderItemVO::getSkuId).collect(Collectors.toList());
            countDownLatch.countDown();
        }, executor);

        // 查询用户积分
        Integer integration = loginUser.getIntegration();
        vo.setIntegration(integration);

        // TODO 防重令牌

        countDownLatch.await();

        return vo;
    }

}