package simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerDemo {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 建立连接
        ConnectionFactory factory = new ConnectionFactory();

        // 2. 设置参数
        factory.setHost("8.156.77.78");   // MQ所在的服务器地址
        factory.setPort(5672);            // 端口号
        factory.setUsername("edison");    // 账号
        factory.setPassword("edison");    // 密码
        factory.setVirtualHost("my_app_vhost");      // 虚拟主机

        // 3. 创建连接connection
        Connection connection = factory.newConnection();

        // 4. 开启 channel 通道
        Channel channel = connection.createChannel();

        // 5. 声明交换机（使用内置的交换机即可）

        // 6. 声明队列
        channel.queueDeclare("my_queue", true, false, false, null);

        // 7. 发送消息
        for (int i = 0; i < 10; i++) {
            String msg = "Hello rabbitmq! " + i;
            channel.basicPublish("", "my_queue", null, msg.getBytes());
        }
        System.out.println("消息发生成功~~~");

        // 8. 资源释放
        channel.close();
        connection.close();
    }
}
