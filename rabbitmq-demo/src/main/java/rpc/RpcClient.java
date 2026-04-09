package rpc;

import com.rabbitmq.client.*;
import constant.Constants;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

/*
    rpc 客户端
    1. 发生请求
    2. 接收响应
 */
public class RpcClient {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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
        channel.queueDeclare(Constants.RPC_RESPONSE_QUEUE, true, false, false, null);

        // 4. 发生请求
        String msg = "hello rpc......";
        // 设置请求的唯一标识
        String correlationId = UUID.randomUUID().toString();
        // 设置请求的相关属性
        AMQP.BasicProperties props = new AMQP.BasicProperties().builder()
                .correlationId(correlationId)
                .replyTo(Constants.RPC_RESPONSE_QUEUE)
                .build();
        channel.basicPublish("", Constants.RPC_REQUEST_QUEUE, props, msg.getBytes());

        // 5. 接收响应
        // 使用阻塞队列来存储响应信息（其实就是等待响应完成）
        final BlockingQueue<String> msgQueue = new ArrayBlockingQueue<>(1);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String respMsg = new String(body);
                System.out.println("接收到回调消息: " + respMsg);
                if (correlationId.equals(properties.getCorrelationId())) {
                    // 如果correlationId校验一致, 说明就是我们想要的响应
                    msgQueue.offer(respMsg);
                }
            }
        };
        channel.basicConsume(Constants.RPC_RESPONSE_QUEUE, true, consumer);
        String result = msgQueue.take();
        System.out.println("[RPC Client 响应结果]: " + result);
    }
}
