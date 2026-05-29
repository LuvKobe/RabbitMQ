package com.edison.extension.listener;

import com.edison.extension.constant.Constants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RetryListener {

    //指定监听队列的名称
    @RabbitListener(queues = Constants.RETRY_QUEUE)
    public void ListenerQueue(Message message) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.printf("接收到消息: %s, deliveryTag: %d \n", new String(message.getBody(),"UTF-8"), deliveryTag);
//        //模拟处理失败
//        int num = 3 / 0;
//        System.out.println("业务处理完成");
        //模拟处理失败
        try {
            int num = 3 / 0;
            System.out.println("业务处理完成");
        }catch (Exception e){
            System.out.println("业务处理失败");
        }
    }

    // 指定监听队列的名称
    // 手动确认
//    @RabbitListener(queues = Constants.RETRY_QUEUE)
//    public void handlerMessage(Message message, Channel channel) throws Exception {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        System.out.printf("接收到消息: %s, deliveryTag: %d \n", new String(message.getBody(),"UTF-8"), deliveryTag);
//        //模拟处理失败
//        try {
//            int num = 3 / 0;
//            System.out.println("业务处理完成");
//            // 手动签收
//            channel.basicAck(deliveryTag, false);
//        }catch (Exception e){
//            // 异常了就拒绝签收
//            // 第三个参数requeue，是否重新发送，如果为true，则会重新发送，若为false，则直接丢弃
//            channel.basicNack(deliveryTag, true, true);
//        }
//    }

}
