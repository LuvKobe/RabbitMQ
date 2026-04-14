package com.edison.rabbitmq.controller;

import com.edison.rabbitmq.constant.Constants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/fanout")
    public String fanout() {
        // routingKey为空, 表示所有队列都可以收到消息
        rabbitTemplate.convertAndSend(Constants.FANOUT_EXCHANGE, "", "hello spring amqp: fanout...");
        return "发送成功";
    }

    @RequestMapping("/direct/{routingKey}")
    public String direct(@PathVariable("routingKey") String routingKey) { // 从路径中拿到routingKey, 需要使用PathVariable注解
        // routingkey作为参数传递
        rabbitTemplate.convertAndSend(Constants.DIRECT_EXCHANGE, routingKey, "hello spring amqp: direct, my routing Key is " + routingKey);
        return "发送成功";
    }

    @RequestMapping("/topic/{routingKey}")
    public String topic(@PathVariable("routingKey") String routingKey) { // 从路径中拿到routingKey, 需要使用PathVariable注解
        // routingkey作为参数传递
        rabbitTemplate.convertAndSend(Constants.TOPIC_EXCHANGE, routingKey, "hello spring amqp: topic, my routing Key is " + routingKey);
        return "发送成功";
    }
}

