package com.zxd.mq.conf;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by zxd on 2019/7/23.
 */
public class ConnectionUtil {

    public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("127.0.0.1");
        factory.setPort(5672);

        factory.setVirtualHost("zxdtest");
        factory.setUsername("zxdadmin");
        factory.setPassword("zxd19871");
        Connection connetion = factory.newConnection();
        return connetion;
    }
}
