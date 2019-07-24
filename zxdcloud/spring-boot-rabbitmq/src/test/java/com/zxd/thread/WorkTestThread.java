package com.zxd.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zxd on 2019/5/8.
 */
public class WorkTestThread implements Runnable {

    private WorkTest worker;
    private CountDownLatch cdLatch;

    public WorkTestThread(WorkTest worker, CountDownLatch cdLatch) {
        this.worker = worker;
        this.cdLatch = cdLatch;
    }

    @Override
    public void run() {
        worker.doWork();        // 让工人开始工作
        cdLatch.countDown();    // 工作完成后倒计时次数减1
    }

}
