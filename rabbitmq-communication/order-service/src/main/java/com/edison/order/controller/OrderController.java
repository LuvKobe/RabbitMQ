package com.edison.order.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
}