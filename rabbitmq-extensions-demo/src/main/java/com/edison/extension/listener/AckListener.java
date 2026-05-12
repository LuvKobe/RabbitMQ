package com.edison.extension.listener;

import com.edison.extension.constant.Constants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class AckListener {

    /**
    @RabbitListener(queues = Constants.ACK_QUEUE)
    public void handMessage(Message message, Channel channel) throws UnsupportedEncodingException {
        // 消费者逻辑
        System.out.printf("接收到消息: %s, deliveryTag: %d \n", new String(message.getBody(), "UTF-8"),
                message.getMessageProperties().getDeliveryTag());

        // 进行业务逻辑处理（模拟即可）
        System.out.println("正在开始进行业务逻辑处理...");
        //int num = 10 / 0;
        System.out.println("处理完成");
    }**/

    @RabbitListener(queues = Constants.ACK_QUEUE)
    public void handMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 1.接收消息
            System.out.printf("接收到消息: %s, deliveryTag: %d \n", new String(message.getBody(), "UTF-8"),
                    message.getMessageProperties().getDeliveryTag());

            // 2.进行业务逻辑处理（模拟即可）
            System.out.println("正在开始进行业务逻辑处理...");
            int num = 10 / 0;
            System.out.println("处理完成");

            // 3.手动签收(肯定确认)
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 4.异常了就拒绝签收
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
