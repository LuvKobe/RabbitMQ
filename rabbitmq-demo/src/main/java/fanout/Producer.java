package fanout;

import com.rabbitmq.client.BuiltinExchangeType;
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
        channel.exchangeDeclare(Constants.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT, true);

        // 4. 声明队列
        channel.queueDeclare(Constants.FANOUT_QUEUE1, true, false, false, null);
        channel.queueDeclare(Constants.FANOUT_QUEUE2, true, false, false, null);

        // 5. 绑定队列和交换机
        channel.queueBind(Constants.FANOUT_QUEUE1, Constants.FANOUT_EXCHANGE, "");
        channel.queueBind(Constants.FANOUT_QUEUE2, Constants.FANOUT_EXCHANGE, "");

        // 6. 发送消息
        String msg = "Hello fanout... ";
        channel.basicPublish(Constants.FANOUT_EXCHANGE, "",null, msg.getBytes());
        System.out.println("消息发送成功");

        // 7. 资源释放
        channel.close();
        connection.close();
    }
}
