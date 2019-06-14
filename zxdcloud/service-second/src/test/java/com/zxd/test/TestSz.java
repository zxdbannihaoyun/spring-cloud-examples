package com.zxd.test;

/**
 * Created by zxd on 2019/6/5.
 */

import org.junit.Test;

public class TestSz {


    @Test
    public void test() {
        int[] arr = {1, 31, 10, 2, 43, 15, 21};
        sortBubbling(arr);
        int i = 0;
        while (i < arr.length) {
            System.out.println(arr[i]);
            i++;
        }
    }

    // 冒泡排序：两个循环，通过两两相比，进行排序
    private void sortBubbling(int[] arr) {
        // 第一轮确定最后一个，第二轮确定倒数第二个...
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                // 两两相比，就像鱼吐水泡一样...
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    @Test
    public void test2() {
        int[] arr = {1, 31, 10, 2, 43, 15, 21};
        sortChange(arr);
        int i = 0;
        while (i < arr.length) {
            System.out.println(arr[i]);
            i++;
        }
    }

    // 选择排序，选择第一个元素和剩下的n-1个比较
    private static void sortChange(int[] arr) {
        // 第一轮确定第一个元素，第二轮确定第二个元素
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                // 选择第一i个元素和剩余的元素进行比较
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    @Test
    public void test03() {
        int[] arr = {1, 31, 10, 2, 43, 15, 21};
        sortBucket(arr);
    }

    // 桶排序，声明一个以 最大元素+1 为长度的数组，遍历原数组，桶数组计数;必须知道数组中最大的数是多少
    private static void sortBucket(int[] arr) {
        int[] arr1 = new int[43 + 1];
        for (int i = 0; i < arr.length; i++) {
            arr1[arr[i]]++;
        }
        for (int i = 0; i < arr1.length; i++) {
            int count = arr1[i];
            while (count > 0) {
                System.out.println(i);
                count--;
            }
        }
    }


}
