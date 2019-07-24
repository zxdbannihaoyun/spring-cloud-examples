package com.zxd.singleton;

/**
 * Created by zxd on 2019/7/11.
 */
public class Demo3 {

    private static Demo3 demo2;

    private Demo3() {

    }

    /**
     * 懒汉模式
     * @return
     */
    public static synchronized Demo3 getDemo2() {
        if (null == demo2) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            demo2 = new Demo3();
        }
        return demo2;
    }
}
