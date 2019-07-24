package com.zxd.mq.basic.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zxd.mq.conf.ConnectionUtil;

/**
 * Created by zxd on 2019/7/24.
 */
public class SendExchange {
    private final static String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 消息内容
        String message = "Hello direct World!";
        channel.basicPublish(EXCHANGE_NAME, "delete", null, message.getBytes());
        System.out.println(" [direct] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
