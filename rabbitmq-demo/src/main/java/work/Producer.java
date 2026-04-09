package work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import constant.Constants;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
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

        // 5. 发送消息
        for (int i = 0; i < 10; i++) {
            String msg = "Hello work queue... " + i;
            channel.basicPublish("", Constants.WORK_QUEUE, null, msg.getBytes());
        }
        System.out.println("消息发生成功~~~");

        // 6. 资源释放
        channel.close();
        connection.close();
    }
}
