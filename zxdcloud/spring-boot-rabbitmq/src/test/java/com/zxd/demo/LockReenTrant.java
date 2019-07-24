package com.zxd.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zxd on 2019/5/21.
 */
public class LockReenTrant {

    private static Integer count = 0;

    private final Integer FULL = 5;

    final Lock lock = new ReentrantLock();

    final Condition put = lock.newCondition();

    final Condition get = lock.newCondition();

    public static void main(String[] args) {
        LockReenTrant lockReenTrant = new LockReenTrant();
        new Thread(lockReenTrant.new Producer()).start();
        new Thread(lockReenTrant.new Consumer()).start();
        new Thread(lockReenTrant.new Producer()).start();
        new Thread(lockReenTrant.new Consumer()).start();

    }

    class Producer implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.lock();
                try {
                    while (count == FULL) {
                        try {
                            put.await();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + "produce:: " + count);
                    get.signal();
                } finally {
                    lock.unlock();
                }
            }


        }
    }

    class Consumer implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }

                lock.lock();
                try {
                    while (count == 0) {
                        try {
                            get.await();
                        } catch (InterruptedException e4) {
                            e4.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println(Thread.currentThread().getName() + "consume:: " + count);
                    put.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }


}
