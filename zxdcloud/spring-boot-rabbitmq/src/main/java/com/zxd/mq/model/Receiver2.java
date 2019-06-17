package com.zxd.mq.model;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "zxdTest2")
//这里的zxdTest2是多对多，如果要测试一对多改成zxdTest1
public class Receiver2 {

    @RabbitHandler
    public void receiver(String msg){
        System.out.println("Test2 receiver2:"+msg);
    }
}
