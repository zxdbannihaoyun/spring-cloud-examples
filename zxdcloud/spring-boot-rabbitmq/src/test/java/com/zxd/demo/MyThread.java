package com.zxd.demo;


/**
 * Created by zxd on 2019/2/21.
 */
public class MyThread extends Thread {

    private int ticket = 5;

    public void run() {
            while (true) {
                System.out.println(Thread.currentThread().getName()+"售票 = " + ticket--);
                if (ticket < 0) {
                    break;
                }
            }
    }



    public static void main(String[] args) {
        MyThread mt = new MyThread();
        new Thread(mt,"线程1").start();
        new Thread(mt,"线程2").start();

    }

}
