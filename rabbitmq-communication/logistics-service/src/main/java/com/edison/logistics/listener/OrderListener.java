package com.edison.logistics.listener;

import com.edison.order.model.OrderInfo;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "order.create")
public class OrderListener {
    // 指定监听队列的名称
    @RabbitHandler
    public void handMessage(String orderInfo) {
        System.out.println("接收到订单消息String: " + orderInfo);
        // 收到消息后的处理, 代码省略
    }

    // 指定监听队列的名称
    @RabbitHandler
    public void handMessage(OrderInfo orderInfo) {
        System.out.println("接收到订单消息orderInfo: " + orderInfo);
        // 收到消息后的处理, 代码省略
    }
}