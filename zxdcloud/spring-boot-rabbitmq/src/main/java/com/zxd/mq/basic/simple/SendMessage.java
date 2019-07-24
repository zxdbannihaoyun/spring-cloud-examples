package com.zxd.mq.basic.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zxd.mq.conf.ConnectionUtil;

/**
 * Created by zxd on 2019/7/24.
 */
public class SendMessage {

    private final static String QUEUE_NAME = "first_queue";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "hello world";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("[x] send '" + message + "'");
        channel.close();
        connection.close();
    }

}
