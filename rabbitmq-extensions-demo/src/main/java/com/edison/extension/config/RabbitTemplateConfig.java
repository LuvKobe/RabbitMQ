package com.edison.extension.config;

import jakarta.annotation.Resource;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTemplateConfig {

    @Bean("rabbitTemplate")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean("confirmRabbitTemplate")
    public RabbitTemplate confirmRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 设置回调方法
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("执行了confirm方法");
                if (ack) {
                    System.out.printf("接收到消息, 消息ID: %s \n", correlationData == null ? null : correlationData.getId());
                } else {
                    System.out.printf("未接收到消息, 消息ID: %s, 原因: %s \n", correlationData == null ? null : correlationData.getId(), cause);
                    // 相应的业务处理......省略
                }
            }
        });

        // 消息被退回时, 回调方法
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                System.out.printf("消息被退回: %s \n", returned);
            }
        });
        return rabbitTemplate;
    }

    // 还可以单独拎出来
    @Bean("returnConfirmRabbitTemplate")
    public RabbitTemplate returnConfirmRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        // 设置回退方法
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                System.out.printf("消息被退回: %s \n", returned);
            }
        });
        return rabbitTemplate;
    }
}