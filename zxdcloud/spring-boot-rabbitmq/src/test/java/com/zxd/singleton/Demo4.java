package com.zxd.singleton;

/**
 * Created by zxd on 2019/7/11.
 */
public class Demo4 {

    private static Demo4 demo4;

    private Demo4() {

    }

    /**
     * 懒汉模式
     *
     * @return
     */
    public static Demo4 getDemo2() {
       try{
           synchronized (Demo4.class) {
               if (null == demo4) {
                   // 模拟在创建对象之前做一些准备工作
                   Thread.sleep(1000);
                   demo4 = new Demo4();
               }
           }
       }catch (InterruptedException e){

       }
        return  demo4;
    }
}
