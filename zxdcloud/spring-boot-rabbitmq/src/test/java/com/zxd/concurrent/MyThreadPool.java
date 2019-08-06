package com.zxd.concurrent;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zxd on 2019/7/11.
 */
public class MyThreadPool {

    private final static Logger logger = LoggerFactory.getLogger(MyThreadPool.class);

    @Test
    public void test01() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        long start = System.currentTimeMillis();
        for (int i = 1; i <= 15; i++) {
            executorService.execute(new MyRunable(i));
        }

        executorService.shutdown();
        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            logger.info("线程还在执行");
        }
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
            System.out.println(ab * 100);
        }
    }
}
