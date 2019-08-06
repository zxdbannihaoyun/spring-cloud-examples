package jdk8;

/**
 * Created by zxd on 2019/8/1.
 */
public class MyThreadPrinter implements Runnable {

    private String name;
    private Object prev;
    private Object self;

    private MyThreadPrinter(String name, Object prev, Object self) {
        this.name = name;
        this.prev = prev;
        this.self = self;

    }

    @Override
    public void run() {
        int count = 10;
        while (count > 0) {
            synchronized (prev) {
                synchronized (self) {
                    System.out.println(name);
                    count--;
                    self.notify();
                }
                try {
                    prev.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();

        MyThreadPrinter pa = new MyThreadPrinter("A", c, a);
        MyThreadPrinter pb = new MyThreadPrinter("B", a, b);
        MyThreadPrinter pc = new MyThreadPrinter("C", b, c);

        new Thread(pa).start();
        Thread.sleep(100);
        new Thread(pb).start();
        Thread.sleep(100);
        new Thread(pc).start();
        Thread.sleep(100);

//        new Thread(pa).run();
//        Thread.sleep(100);
//        new Thread(pb).run();
//        Thread.sleep(100);
//        new Thread(pc).run();
//        Thread.sleep(100);
    }
}
