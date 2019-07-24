package com.zxd.demo;

import java.util.*;

/**
 * Created by zxd on 2019/1/31.
 */
public class Test {
    public static void main(String[] args) {
//        int times = 125;
//        int num = 60;
//        int re = times/num;
//        int rr = times%num;
//        System.out.printf("结果"+re);
//        System.out.printf("结果"+rr);
//        DateTime dt = new DateTime();
//        String str = dt.toString("yyyy-MM-dd HH:mm:ss");
//        System.out.println(str);

//        DateTime dt = new DateTime(new Date());
//        System.out.println(dt.toString("yyyy-MM-dd HH:mm:ss"));
        Hashtable ht = new Hashtable();



//        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//        map.put(1, 8);
//        map.put(3, 12);
//        map.put(5, 53);
//        map.put(123, 33);
//        map.put(42, 11);
//        map.put(44, 42);
//        map.put(15, 3);
//
//        System.out.println(getMaxKey(map));
//        System.out.println(getMaxValue(map));

    }

    /**
     * 求Map<K,V>中Key(键)的最大值
     *
     * @param map
     * @return
     */
    public static Object getMaxKey(Map<Integer, Integer> map) {
        if (map == null) return null;
        Set<Integer> set = map.keySet();
        Object[] obj = set.toArray();
        Arrays.sort(obj);
        return obj[obj.length - 1];
    }

    /**
     * 求Map<K,V>中Value(值)的最大值
     *
     * @param map
     * @return
     */
    public static Object getMaxValue(Map<Integer, Integer> map) {
        if (map == null) return null;
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        return obj[obj.length - 1];
    }

}
