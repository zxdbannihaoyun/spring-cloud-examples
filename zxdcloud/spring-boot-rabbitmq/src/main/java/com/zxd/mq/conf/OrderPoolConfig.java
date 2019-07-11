package com.zxd.mq.conf;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by zxd on 2019/7/11.
 */
@Configuration
public class OrderPoolConfig {
    /**
     * 创建线程池；setDaemon是否是守护进程，setPriority优先级别
     * @return
     */
    @Bean(value = "orderThreadPool")
    public ExecutorService buildOrderQueueThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setDaemon(false).setNameFormat("order").setPriority(2).build();

        ExecutorService pool = Executors.newFixedThreadPool(5, namedThreadFactory);
        return pool;
    }
}
