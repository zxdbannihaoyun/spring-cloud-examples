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
public class ThreadPoolConfig {
    /**
     * 创建线程池；setDaemon是否是守护进程，setPriority优先级别
     * @return
     */
    @Bean(value = "consumerQueueThreadPool")
    public ExecutorService buildConsumerQueueThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setDaemon(false).setNameFormat("zxd").setPriority(1).build();

        ExecutorService pool = Executors.newFixedThreadPool(10, namedThreadFactory);
        return pool;
    }
}
