package rpc;

import com.rabbitmq.client.*;
import constant.Constants;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    * RPC 服务端
    * 1. 接收请求
    * 2. 发生响应
 */
public class RpcServer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 建立连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.HOST);   // MQ所在的服务器地址
        factory.setPort(Constants.PORT);            // 端口号
        factory.setUsername(Constants.USERNAME);    // 账号
        factory.setPassword(Constants.PASSWORD);    // 密码
        factory.setVirtualHost(Constants.VIRTUAL_HOST);      // 虚拟主机
        Connection connection = factory.newConnection();

        // 2. 开启 channel 通道
        Channel channel = connection.createChannel();

        // 3. 声明队列
        channel.queueDeclare(Constants.RPC_REQUEST_QUEUE, true, false, false, null);

        // 4. 接收请求
        channel.basicQos(1);
        System.out.println("Awaiting RPC request...");
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String request = new String(body, "UTF-8");
                System.out.println("接收到请求: " + request);
                String response = "针对request: " + request + ", 响应成功";
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                        .correlationId(properties.getCorrelationId())
                        .build();
                channel.basicPublish("", Constants.RPC_RESPONSE_QUEUE, basicProperties, response.getBytes());
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(Constants.RPC_REQUEST_QUEUE, false, consumer);
    }
}