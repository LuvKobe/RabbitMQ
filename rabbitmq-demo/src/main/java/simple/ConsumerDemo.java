package simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ConsumerDemo {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 1. 建立连接
        ConnectionFactory factory = new ConnectionFactory();

        // 2. 设置参数
        factory.setHost("8.156.77.78");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_app_vhost");

        // 3. 创建连接connection
        Connection connection = factory.newConnection();

        // 4. 开启 channel 通道
        Channel channel = connection.createChannel();

        // 5. 声明一个队列Queue
        channel.queueDeclare("my_queue", true, false, false, null);

        // 6. 消费消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /*
            回调方法，当收到消息后，会自动执行该方法：
            1. consumerTag：标识
            2. envelope：获取一些信息，如交换机、路由key
            3. properties：配置信息
            4. body：数据
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //
                System.out.println("接收到消息: " + new String(body));
            }
        };
        channel.basicConsume("my_queue", true, consumer);

        // 7. 释放资源
        TimeUnit.SECONDS.sleep(5); // 等待回调函数执行完毕之后，关闭资源
        channel.close();
        connection.close();
    }
}
