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
    /**
    @Bean("presQueue")
    public Queue presQueue() {
        return QueueBuilder.nonDurable(Constants.PRES_QUEUE).build();
    }*/
    // 持久化的队列
    @Bean("presQueue")
    public Queue presQueue() {
        return QueueBuilder.durable(Constants.PRES_QUEUE).build();
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

    // 发送方确认
    // 声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(Constants.CONFIRM_QUEUE).build();
    }
    // 声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(Constants.CONFIRM_EXCHANGE).build();
    }
    // 声明绑定关系
    @Bean("confirmBinding")
    public Binding confirmBinding(@Qualifier("confirmQueue") Queue queue, @Qualifier("confirmExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("confirm").noargs();
    }

    // 重试机制
    // 声明队列
    @Bean("retryQueue")
    public Queue retryQueue() {
        return QueueBuilder.durable(Constants.RETRY_QUEUE).build();
    }
    // 声明交换机
    @Bean("retryExchange")
    public DirectExchange retryExchange() {
        return ExchangeBuilder.directExchange(Constants.RETRY_EXCHANGE).build();
    }
    // 声明绑定关系
    @Bean("retryBinding")
    public Binding retryBinding(@Qualifier("retryQueue") Queue queue, @Qualifier("retryExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("retry").noargs();
    }

    // 消息的TTL
    //1. 队列
    @Bean("ttlQueue")
    public Queue ttlQueue() {
        return QueueBuilder.durable(Constants.TTL_QUEUE).build();
    }
    //2. 交换机
    @Bean("ttlExchange")
    public Exchange ttlExchange() {
        return ExchangeBuilder.fanoutExchange(Constants.TTL_EXCHANGE).durable(true).build();
    }
    //3. 队列和交换机绑定
    @Bean("ttlBinding")
    public Binding ttlBinding(@Qualifier("ttlQueue") Queue queue, @Qualifier("ttlExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ttl").noargs();
    }

    //设置队列TTL
    @Bean("ttlQueue2")
    public Queue ttlQueue2() {
        //设置20秒过期
        return QueueBuilder.durable(Constants.TTL_QUEUE2).ttl(20*1000).build();
    }
    //队列和交换机绑定
    @Bean("ttlBinding2")
    public Binding ttlBinding2(@Qualifier("ttlQueue2") Queue queue, @Qualifier("ttlExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ttl").noargs();
    }
}
