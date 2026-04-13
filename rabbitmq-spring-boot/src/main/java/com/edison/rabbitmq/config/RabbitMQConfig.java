package com.edison.rabbitmq.config;

import com.edison.rabbitmq.constant.Constants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // 1. 工作模式队列
    @Bean("workQueue")
    public Queue workQueue() {
        return QueueBuilder.durable(Constants.WORK_QUEUE).build();
    }

    // 2. 发布订阅模式
    // 声明2个队列, 观察是否两个队列都收到了消息
    @Bean("fanoutQueue1")
    public Queue fanoutQueue1() {
        return QueueBuilder.durable(Constants.FANOUT_QUEUE1).build();
    }
    @Bean("fanoutQueue2")
    public Queue fanoutQueue2() {
        return QueueBuilder.durable(Constants.FANOUT_QUEUE2).build();
    }
    // 声明交换机
    @Bean("fanoutExchange")
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange(Constants.FANOUT_EXCHANGE).durable(true).build();
    }
    // 声明交换机和队列的绑定
    @Bean("fanoutQueueBinding1")
    public Binding fanoutQueueBinding1(@Qualifier("fanoutExchange") FanoutExchange fanoutExchange, @Qualifier("fanoutQueue1") Queue queue) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
    @Bean("fanoutQueueBinding2")
    public Binding fanoutQueueBinding2(@Qualifier("fanoutExchange") FanoutExchange fanoutExchange, @Qualifier("fanoutQueue2") Queue queue) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}


