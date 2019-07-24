package com.zxd.singleton;

/**
 * Created by zxd on 2019/7/11.
 */
public class Demo1 {

    private static Demo1 demo1 = new Demo1();

    private Demo1() {

    }

    /**
     * 饿汉模式
     * @return
     */
    public static Demo1 getDemo1() {
        return demo1;
    }
}
