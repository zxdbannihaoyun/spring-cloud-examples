package com.zxd.demo;

/**
 * Created by zxd on 2019/5/21.
 */
public class LockSynTest {

    private static Integer count = 0;

    private final Integer FULL = 5;

    private static String lock = "lock";

    public static void main(String[] args) {
        LockSynTest lockSynTest = new LockSynTest();

        new Thread(lockSynTest.new Producer()).start();
        new Thread(lockSynTest.new Consumer()).start();
        new Thread(lockSynTest.new Producer()).start();
        new Thread(lockSynTest.new Consumer()).start();

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
                synchronized (lock) {
                    while (count == FULL) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + "produce:: " + count);
                    lock.notifyAll();
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
                synchronized (lock) {
                    while (count == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e4) {
                            e4.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println(Thread.currentThread().getName() + "consume:: " + count);
                    lock.notifyAll();
                }
            }
        }
    }
}
