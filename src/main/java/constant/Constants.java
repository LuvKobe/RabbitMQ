package constant;

public class Constants {
    public static final String HOST = "8.156.77.78";
    public static final int PORT = 5672;
    public static final String USERNAME = "edison";
    public static final String PASSWORD = "edison";
    public static final String VIRTUAL_HOST = "my_app_vhost";

    // 工作队列模式
    public static final String WORK_QUEUE = "work.queue";

    // 发布订阅模式
    public static final String FANOUT_EXCHANGE = "fanout.exchange"; // 声明交换机
    public static final String FANOUT_QUEUE1 = "fanout.queue1"; // 声明队列
    public static final String FANOUT_QUEUE2 = "fanout.queue2"; // 声明队列
}



