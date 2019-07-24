package com.zxd.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zxd on 2019/5/8.
 */
public class TestWork {

    private static final int MAX_WORK_DURATION = 5000;

    private static final int MIN_WORK_DURATION = 1000;

    private static long getRandomWorkDuration(long min, long max) {
        return (long) (Math.random() * (max - min) + min);
    }

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(2);   // 创建倒计时闩并指定倒计时次数为2
        WorkTest w1 = new WorkTest("骆昊", getRandomWorkDuration(MIN_WORK_DURATION, MAX_WORK_DURATION));
        WorkTest w2 = new WorkTest("王大锤", getRandomWorkDuration(MIN_WORK_DURATION, MAX_WORK_DURATION));

        new Thread(new WorkTestThread(w1, latch)).start();
        new Thread(new WorkTestThread(w2, latch)).start();

        try {
            latch.await();  // 等待倒计时闩减到0
            System.out.println("All jobs have been finished!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
