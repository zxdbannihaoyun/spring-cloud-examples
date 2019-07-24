package com.zxd.singleton;

/**
 * Created by zxd on 2019/7/11.
 */
public class Demo2 {

    private static Demo2 demo2;

    private Demo2() {

    }

    /**
     * 懒汉模式
     * @return
     */
    public static Demo2 getDemo2() {
        if (null == demo2) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            demo2 = new Demo2();
        }
        return demo2;
    }
}
