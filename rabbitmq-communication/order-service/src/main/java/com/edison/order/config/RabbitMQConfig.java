package com.edison.order.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean("orderQueue")
    public Queue orderQueue() {
        return QueueBuilder.durable("order.create").build();
    }
}
