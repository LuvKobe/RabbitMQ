package com.edison.extension.listener;

import com.edison.extension.constant.Constants;
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
}
