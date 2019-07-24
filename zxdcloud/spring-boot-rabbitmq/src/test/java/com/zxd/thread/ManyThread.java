package com.zxd.thread;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zxd on 2019/5/8.
 */
public class ManyThread {

    @Test
    public void test() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<String> results = Lists.newArrayList();
        for (int i = 1; i <= 30; i++) {
            String tar_str = "ABC_" + i;
            Future<String> model = service.submit(new MyCallables(tar_str));
            String result = model.get();
            System.out.println("打印第" + i + "的结果：" + result);
            results.add(result);
        }
        service.shutdown();

        while (!service.isTerminated()) {
        }

//        while (true) {
//            if (service.isTerminated()) {
//                System.out.println("执行到了这里！！！！！！！");
//                break;
//            }
//            // 为避免一直循环 加延迟 后再判断
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }


    class MyCallables implements Callable<String> {

        private String target;

        public MyCallables(String target) {
            this.target = target;
        }

        @Override
        public String call() throws Exception {
            System.out.println(target);
            StringBuffer sb = new StringBuffer(target);
            String str = sb.reverse().toString();
            return str;
        }
    }
}
