package com.edison.rabbitmq.controller;

import com.edison.rabbitmq.constant.Constants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/producer")
@RestController
public class ProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate; // 可以理解为rabbitmq客户端

    @RequestMapping("/work")
    public String work() {
        for (int i = 0; i < 10; i ++)
        {
            // 使用内置交换机, RoutingKey和队列名称一致
            rabbitTemplate.convertAndSend("", Constants.WORK_QUEUE, "hello spring amqp: work...");
        }
        return "发送成功";
    }
}
