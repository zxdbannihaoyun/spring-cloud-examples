package com.zxd.concurrent;

import com.zxd.mq.MqApplication;
import com.zxd.mq.conf.ThreadPoolConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zxd on 2019/7/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MqApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestThreadPool {

    private final static Logger logger = LoggerFactory.getLogger(TestThreadPool.class);

    @Autowired
    private ThreadPoolConfig threadPoolConfig;


    @Test
    public void test() throws Exception {
        ExecutorService pool = threadPoolConfig.buildConsumerQueueThreadPool();
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 15; i++) {
            pool.execute(new TestThreadPool.MyRunable(i));
        }
        pool.shutdown();//关闭线程池，不在接受新开启的线程，但是会执行已接收的线程
        while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
            //每隔1秒监视一下是否线程池线程都执行完毕
            logger.info("线程还在执行");
        }
        //线程池里的线程全部执行完毕
        long end = System.currentTimeMillis();
        logger.info("一共处理了【{}】", (end - start));
    }

    class MyRunable implements Runnable {

        private int ab = 1;

        public MyRunable(int c) {
            ab = c;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println("线程名字：" + name + "####" + ab * 100);
        }
    }
}
