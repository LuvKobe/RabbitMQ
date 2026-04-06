package publisher.confirms;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import constant.Constants;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class PublisherConfirms {
    private static final Integer MESSAGE_COUNT = 20000; // 测试的消息数

    static Connection createConnection() throws IOException, TimeoutException {
        // 建立连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.HOST);   // MQ所在的服务器地址
        factory.setPort(Constants.PORT);            // 端口号
        factory.setUsername(Constants.USERNAME);    // 账号
        factory.setPassword(Constants.PASSWORD);    // 密码
        factory.setVirtualHost(Constants.VIRTUAL_HOST);      // 虚拟主机
        return factory.newConnection();
    }

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // 策略一：单独确认
        publishingMessagesIndividually();

        // 策略二：批量确认
        publishingMessagesInBatches();

        // 策略一：异步确认
        handlingPublisherConfirmsAsynchronously();
    }

    /**
     * 异步确认
     */
    private static void handlingPublisherConfirmsAsynchronously() throws IOException, TimeoutException, InterruptedException {
        try(Connection connection = createConnection()) {
            // 1. 开启信道
            Channel channel = connection.createChannel();

            // 2. 设置信道为confirm模式
            channel.confirmSelect();

            // 3. 声明队列
            channel.queueDeclare(Constants.PUBLISHER_CONFIRMS_QUEUE3, true, false, false, null);

            // 4. 监听confirm
            long startTime = System.currentTimeMillis();
            // 有序集合, 元素按照⾃然顺序进⾏排序, 集合中存储的是未确认的消息ID
            SortedSet<Long> confirmSeqNo = Collections.synchronizedSortedSet(new TreeSet<>());

            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    if (multiple) {
                        // 批量确认:将集合中⼩于等于当前序号deliveryTag元素的集合清除，表⽰这批序号的消息都已经被ack了
                        confirmSeqNo.headSet(deliveryTag + 1).clear();
                    }
                    else {
                        //单条确认:将当前的deliveryTag从集合中移除
                        confirmSeqNo.remove(deliveryTag);
                    }
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    if (multiple) {
                        // 批量确认:将集合中⼩于等于当前序号deliveryTag元素的集合清除，表⽰这批序号的消息都已经被ack了
                        confirmSeqNo.headSet(deliveryTag + 1).clear();
                    }
                    else {
                        //单条确认:将当前的deliveryTag从集合中移除
                        confirmSeqNo.remove(deliveryTag);
                    }
                    //如果处理失败,这⾥需要添加处理消息重发的场景, 此处代码省略
                    //TODO......
                }
            });

            // 5. 发生消息
            // 循环发送消息
            for (int i = 0; i <MESSAGE_COUNT; i++) {
                String message = "Hello publisher confirms " + i;
                // 得到下次发送消息的序号, 从1开始
                long seqNo = channel.getNextPublishSeqNo();
                channel.basicPublish("", Constants.PUBLISHER_CONFIRMS_QUEUE3, null, message.getBytes());

                // 将序号存⼊集合中
                confirmSeqNo.add(seqNo);
            }
            // 消息确认完毕
            while (!confirmSeqNo.isEmpty()) {
                Thread.sleep(100); // 休眠100毫秒
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("异步确认策略, 消息条数: %d, 耗时: %d ms\n", MESSAGE_COUNT, endTime - startTime);
        }
    }

    /**
     * 批量确认
     */
    private static void publishingMessagesInBatches() throws IOException, TimeoutException, InterruptedException {
        try(Connection connection = createConnection()) {
            // 1. 开启信道
            Channel channel = connection.createChannel();

            // 2. 设置信道为confirm模式
            channel.confirmSelect();

            // 3. 声明队列
            channel.queueDeclare(Constants.PUBLISHER_CONFIRMS_QUEUE2, true, false, false, null);

            // 4. 发生消息, 并进行确认
            long startTime = System.currentTimeMillis();
            int batchSize = 100; // 设置批量处理的大小(每100条就确认一次)
            int  outStandingMessageCount = 0; // 计数器
            for (int i = 0; i <MESSAGE_COUNT; i++) {
                // 发生消息
                String message = "Hello publisher confirms " + i;
                channel.basicPublish("", Constants.PUBLISHER_CONFIRMS_QUEUE2, null, message.getBytes());
                outStandingMessageCount++;
                // 批量确认消息
                if (outStandingMessageCount == batchSize) {
                    channel.waitForConfirmsOrDie(5000); // 等待5秒
                    outStandingMessageCount = 0; // 重置计数器
                }
            }
            // 消息发送完, 还有未确认的消息, 进行确认
            if(outStandingMessageCount > 0) {
                channel.waitForConfirmsOrDie(5000);
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("批量确认策略, 消息条数: %d, 耗时: %d ms\n", MESSAGE_COUNT, endTime - startTime);
        }
    }

    /**
     * 单独确认
     */
    private static void publishingMessagesIndividually() throws IOException, TimeoutException, InterruptedException {
        try(Connection connection = createConnection()) {
            // 1. 开启信道
            Channel channel = connection.createChannel();

            // 2. 设置信道为confirm模式
            channel.confirmSelect();

            // 3. 声明队列
            channel.queueDeclare(Constants.PUBLISHER_CONFIRMS_QUEUE1, true, false, false, null);

            // 4. 发生消息, 等待确认
            long startTime = System.currentTimeMillis();
            for (int i = 0; i <MESSAGE_COUNT; i++) {
                // 发生消息
                String message = "Hello publisher confirms " + i;
                channel.basicPublish("", Constants.PUBLISHER_CONFIRMS_QUEUE1, null, message.getBytes());

                // 等待确认
                channel.waitForConfirmsOrDie(5000); // 等待5秒
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("单独确认策略, 消息条数: %d, 耗时: %d ms\n", MESSAGE_COUNT, endTime - startTime);
        }
    }
}