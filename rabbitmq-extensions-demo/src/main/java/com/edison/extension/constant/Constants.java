package com.edison.extension.constant;

public class Constants {

    // 配置队列和交换机
    public static final String ACK_QUEUE = "ack.queue";
    public static final String ACK_EXCHANGE = "ack.exchange";

    public static final String PRES_QUEUE = "pref.queue";
    public static final String PRES_EXCHANGE = "pref.exchange";

    // 发送方确认
    public static final String CONFIRM_QUEUE = "confirm.queue";
    public static final String CONFIRM_EXCHANGE = "confirm.exchange";

    // 重试机制
    public static final String RETRY_QUEUE =  "retry.queue";
    public static final String RETRY_EXCHANGE = "retry.exchange";

    // TTL
    public static final String TTL_QUEUE =  "ttl.queue";
    public static final String TTL_QUEUE2 = "ttl.queue2";
    public static final String TTL_EXCHANGE = "ttl.exchange";
}
