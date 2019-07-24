package com.zxd.thread;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zxd on 2019/5/8.
 */
public class Account {

    private double balance;

    @Test
    public void test01() {//1.测试线程不同步，方法不同步 返回结果：不等于100元，方法同步返回100元
        Account account = new Account();
        ExecutorService service = Executors.newFixedThreadPool(100);

        for (int i = 1; i <= 100; i++) {
            service.execute(new AddMoneyThread(account, 1));
        }
        service.shutdown();

        while (!service.isTerminated()) {
        }

        System.out.println("账户余额: " + account.getBalance());
    }

    @Test
    public void test02() {//2.测试线程不同步，带锁的存钱方法
        RealAccount account = new RealAccount();
        ExecutorService service = Executors.newFixedThreadPool(100);
        for (int i = 1; i <= 100; i++) {
            service.execute(new AddRealMoneyThread(account, 1));
        }
        service.shutdown();

        while (!service.isTerminated()) {

        }
        System.out.println("账户余额: " + account.getBalance());
    }

    /**
     * 存款 非同步的存钱方法
     *
     * @param money 存入金额
     */
    public void saveMoney(double money) {
        double newBalance = balance + money;
        try {
            Thread.sleep(10);   // 模拟此业务需要一段处理时间
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        balance = newBalance;
    }

    /**
     * 存款 非同步的存钱方法
     *
     * @param money 存入金额
     */
    public synchronized void saveSysMoney(double money) {
        double newBalance = balance + money;
        try {
            Thread.sleep(10);   // 模拟此业务需要一段处理时间
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        balance = newBalance;
    }

    /**
     * 获得账户余额
     */
    public double getBalance() {
        return balance;
    }


    /**
     * 存钱线程
     */
    class AddMoneyThread implements Runnable {
        private Account account;    // 存入账户
        private double money;       // 存入金额

        public AddMoneyThread(Account account, double money) {
            this.account = account;
            this.money = money;
        }

        @Override
        public void run() {
            account.saveMoney(money);//方法不同步
//            account.saveSysMoney(money);//方法同步
        }

    }

    /**
     * 存钱线程
     */
    class AddRealMoneyThread implements Runnable {
        private RealAccount account;    // 存入账户
        private double money;       // 存入金额

        public AddRealMoneyThread(RealAccount account, double money) {
            this.account = account;
            this.money = money;
        }

        @Override
        public void run() {
            account.realSaveMoney(money);
        }

    }

}
