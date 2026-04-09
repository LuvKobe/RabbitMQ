package work;

import com.rabbitmq.client.*;
import constant.Constants;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer1 {
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

        // 3. 声明交换机（使用内置的交换机即可）
        // ....

        // 4. 声明队列(如果队列不存在，则创建; 反之，存在就创建)
        channel.queueDeclare(Constants.WORK_QUEUE, true, false, false, null);

        // 5. 消费消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 从队列中收到消息后, 就会执行的方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 收到消息以后就进行打印
                System.out.println("接收到消息: " + new String(body));
            }
        };
        channel.basicConsume(Constants.WORK_QUEUE, true, consumer);

        // 8. 资源释放
        channel.close();
        connection.close();
    }
}
