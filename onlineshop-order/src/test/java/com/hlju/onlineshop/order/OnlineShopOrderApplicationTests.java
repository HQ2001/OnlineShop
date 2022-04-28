package com.hlju.onlineshop.order;

import com.hlju.onlineshop.order.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class OnlineShopOrderApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void sendMessageTest() {
        for (int i = 0; i < 10; i++) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(12312L);
            orderEntity.setUserName("asda");
            orderEntity.setReceiverName("jjjjjjjj");
            rabbitTemplate.convertAndSend("hello.java.exchange", "hello.java", orderEntity);
            log.info("消息发送完成: {}", "Hello Word");
        }
    }

    @Test
    void createExchangeTest() {
        DirectExchange exchange = new DirectExchange("hello.java.exchange", true, false);
        amqpAdmin.declareExchange(exchange);
        log.info("exchange  {}  创建成功", exchange.getName());
    }

    @Test
    void createQueueTest() {
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        log.info("queue  {}  创建成功", queue.getName());
    }

    @Test
    void createBindingTest() {
        Binding binding = new Binding(
                "hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello.java.exchange",
                "hello.java",
                null
        );
        amqpAdmin.declareBinding(binding);
        log.info("binding  {}  创建成功", binding.getDestination());
    }

}
