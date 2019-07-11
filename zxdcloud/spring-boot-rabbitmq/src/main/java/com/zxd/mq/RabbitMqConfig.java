package com.zxd.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zxd on 2019/6/17.
 */
public class RabbitMqConfig {
    @Bean
    public Queue Queue1() {
        return new Queue("zxdTest1");
    }
    @Bean
    public Queue Queue2() {
        return new Queue("zxdTest2");
    }
}
