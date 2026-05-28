package com.edison.extension.controller;

import com.edison.extension.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/producer")
@RestController
public class ProducerController {

    @Resource(name = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Resource(name = "confirmRabbitTemplate")
    private RabbitTemplate confirmRabbitTemplate; // confirm模式

    @Resource(name = "returnConfirmRabbitTemplate")
    private RabbitTemplate returnConfirmRabbitTemplate;

    @RequestMapping("/ack")
    public String ack() {
        rabbitTemplate.convertAndSend(Constants.ACK_EXCHANGE, "ack", "consumer ack mode test...");
        return "消息发送成功";
    }

    @RequestMapping("/pres")
    public String pres() {
        Message message = new Message("Presistent test...".getBytes(), new MessageProperties());

        // 消息非持久化
        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);

        // 消息持久化
        //message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        System.out.println(message);
        rabbitTemplate.convertAndSend(Constants.PRES_EXCHANGE, "pres", message);
        return "消息发送成功";
    }

//    @RequestMapping("/confirm")
//    public String confirm() {
//        // 设置回调方法
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                System.out.println("执行了confirm方法");
//                if (ack) {
//                    System.out.printf("接收到消息, 消息ID: %s \n", correlationData == null ? null : correlationData.getId());
//                } else {
//                    System.out.printf("未接收到消息, 消息ID: %s, 原因: %s \n", correlationData == null ? null : correlationData.getId(), cause);
//                    // 相应的业务处理......省略
//                }
//            }
//        });
//        CorrelationData correlationData = new CorrelationData("1");
//        rabbitTemplate.convertAndSend(Constants.CONFIRM_EXCHANGE + "1", "confirm", "consumer confirm mode test...", correlationData);
//        return "消息发送成功";
//    }
    @RequestMapping("/confirm")
    public String confirm() {
        CorrelationData correlationData = new CorrelationData("1");
        confirmRabbitTemplate.convertAndSend(Constants.CONFIRM_EXCHANGE, "confirm", "consumer confirm mode test...", correlationData);
        // Constants.CONFIRM_EXCHANGE + "1" ---> 设置错误的交换机
        //confirmRabbitTemplate.convertAndSend(Constants.CONFIRM_EXCHANGE + "1", "confirm", "consumer confirm mode test...", correlationData);
        return "消息发送成功";
    }

    @RequestMapping("/returns")
    public String returns() {
        CorrelationData correlationData = new CorrelationData("5");
        //returnConfirmRabbitTemplate.convertAndSend(Constants.CONFIRM_EXCHANGE, "confirm", "message return test...", correlationData);
        // 绑定错误的key
        returnConfirmRabbitTemplate.convertAndSend(Constants.CONFIRM_EXCHANGE, "confirm111", "message return test...", correlationData);
        return "消息发送成功";
    }
}
