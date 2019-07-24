package com.zxd.mq.basic.exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zxd.mq.conf.ConnectionUtil;

/**
 * Created by zxd on 2019/7/24.
 */
public class SendExchange {
    private final static String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 消息内容
        String message = "Hello mq World!";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" [fanout] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
