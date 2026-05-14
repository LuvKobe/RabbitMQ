package com.edison.extension.config;

import com.edison.extension.constant.Constants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 队列
    @Bean("ackQueue")
    public Queue ackQueue() {
        return QueueBuilder.durable(Constants.ACK_QUEUE).build();
    }
    // 交换机
    @Bean("directExchange")
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(Constants.ACK_EXCHANGE).build();
    }
    // 队列和交换机绑定
    @Bean("ackBinding")
    public Binding ackBinding(@Qualifier("directExchange") DirectExchange directExchange, @Qualifier("ackQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with("ack");
    }

    // 持久化
    // 非持久化的队列
    @Bean("presQueue")
    public Queue presQueue() {
        return QueueBuilder.nonDurable(Constants.PRES_QUEUE).build();
    }

    // 非持久化的交换机
    @Bean("presExchange")
    public DirectExchange presExchange() {
        return ExchangeBuilder.directExchange(Constants.PRES_EXCHANGE).durable(false).build();
    }

    // 绑定
    @Bean("presBinding")
    public Binding presBinding(@Qualifier("presQueue") Queue queue, @Qualifier("presExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("pres").noargs();
    }
}
