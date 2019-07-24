package com.zxd.thread;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zxd on 2019/5/8.
 */
public class RealAccount {

    private Lock accountLock = new ReentrantLock();

    private double balance;

    @Test
    public void test() {
        //测试线程同步，带锁的方法返回 结果100元
        RealAccount account = new RealAccount();
        ExecutorService service = Executors.newFixedThreadPool(100);

        for (int i = 1; i <= 100; i++) {
            service.execute(new RealAccount.RealAddMoneyThread(account, 1));
        }
        service.shutdown();

        while (!service.isTerminated()) {
        }

        System.out.println("账户余额: " + account.getBalance());
    }

    @Test
    public void test01() {
        //测试线程同步，方法不同步 返回结果100元
        Account account = new Account();
        ExecutorService service = Executors.newFixedThreadPool(100);

        for (int i = 1; i <= 100; i++) {
            service.execute(new RealAccount.AddMoneyThread(account, 1));
        }
        service.shutdown();

        while (!service.isTerminated()) {
        }

        System.out.println("账户余额: " + account.getBalance());
    }


    /**
     * 存款
     *
     * @param money 存入金额
     */
    public void realSaveMoney(double money) {
        accountLock.lock();

        try {
            double newBalance = this.balance + money;

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            balance = newBalance;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            accountLock.unlock();
        }


    }

    /**
     * 获得账户余额
     */
    public double getBalance() {
        return balance;
    }

    /**
     * 存钱线程 同步的
     */
    class RealAddMoneyThread implements Runnable {
        private RealAccount account;    // 存入账户
        private double money;       // 存入金额

        public RealAddMoneyThread(RealAccount account, double money) {
            this.account = account;
            this.money = money;
        }

        @Override
        public void run() {
            synchronized (account) {
                account.realSaveMoney(money);
            }
        }

    }

    /**
     * 存钱线程 同步的
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
            synchronized (account) {
                account.saveMoney(money);//非同步的存钱方法
            }
        }

    }
}
