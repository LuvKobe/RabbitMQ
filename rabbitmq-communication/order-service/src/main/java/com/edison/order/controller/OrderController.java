package com.edison.order.controller;

import com.edison.order.model.OrderInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private RabbitTemplate rabbitTemplate; // 把rabbitmq的客户端注入进来

    @RequestMapping("/create")
    public String create() {
        // 下单相关操作, ⽐如参数校验, 操作数据库等. 代码省略
        // 发送消息通知
        String orderId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend("", "order.create", "下单成功, 订单ID: "+orderId);
        return "下单成功";
    }

    @RequestMapping("/create2")
    public String create2() {
        // 下单相关操作, ⽐如参数校验, 操作数据库等. 代码省略
        // 发送消息通知
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(UUID.randomUUID().toString());
        orderInfo.setName("商品"+new Random().nextInt(100)); // 生成100以内的随机数
        orderInfo.setPrice(ThreadLocalRandom.current().nextLong(1, 51)+"元");
        rabbitTemplate.convertAndSend("", "order.create", orderInfo);
        return "下单成功";
    }
}

