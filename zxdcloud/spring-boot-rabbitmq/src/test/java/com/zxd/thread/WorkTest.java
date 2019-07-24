package com.zxd.thread;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zxd on 2019/5/8.
 */
public class WorkTest {

    private String name;

    private long workDuration;

    private static final int MAX_WORK_DURATION = 5000;

    private static final int MIN_WORK_DURATION = 1000;


    public WorkTest(String name, long workDuration) {
        this.name = name;
        this.workDuration = workDuration;
    }

    private long getRandomWorkDuration(long min, long max) {
        return (long) (Math.random() * (max - min) + min);
    }

    @Test
    public void test() {
        CountDownLatch latch = new CountDownLatch(2);   // 创建倒计时闩并指定倒计时次数为2
        WorkTest w1 = new WorkTest("骆昊", getRandomWorkDuration(MIN_WORK_DURATION, MAX_WORK_DURATION));
        WorkTest w2 = new WorkTest("王大锤", getRandomWorkDuration(MIN_WORK_DURATION, MAX_WORK_DURATION));

        new Thread(new WorkTest.WorkerTestThread(w1, latch)).start();
        new Thread(new WorkTest.WorkerTestThread(w2, latch)).start();

        try {
            latch.await();  // 等待倒计时闩减到0
            System.out.println("All jobs have been finished!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 完成工作
     */
    public void doWork() {
        System.out.println(name + " begins to work...");
        try {
            Thread.sleep(workDuration); // 用休眠模拟工作执行的时间
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println(name + " has finished the job...");
    }

    class WorkerTestThread implements Runnable {
        private WorkTest worker;
        private CountDownLatch cdLatch;

        public WorkerTestThread(WorkTest worker, CountDownLatch cdLatch) {
            this.worker = worker;
            this.cdLatch = cdLatch;
        }

        @Override
        public void run() {
            worker.doWork();        // 让工人开始工作
            cdLatch.countDown();    // 工作完成后倒计时次数减1
        }
    }
}
