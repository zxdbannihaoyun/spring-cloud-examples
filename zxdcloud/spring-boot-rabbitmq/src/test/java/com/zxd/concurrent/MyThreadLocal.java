package com.zxd.concurrent;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zxd on 2019/7/31.
 */
public class MyThreadLocal {

    @Test
    public void test() throws Exception {
//        ThreadLocal threadLocal = new ThreadLocal();
//        Thread myThread = new Thread();
//        myThread.getName();

        LinkedList<String> lk = new LinkedList<String>();

        lk.add("1111");
        lk.add("22323");
        lk.add(1, "4444");


        lk.stream().forEach(e -> System.out.println(e));
    }

    @Test
    public void test2() throws Exception {
        List<String> list = Arrays.asList("a", "b", "c", "d");
        list.forEach(MyThreadLocal::printValur);

    }

    private static void  printValur(String str){
        System.out.println("print value : "+str);
    }
}
