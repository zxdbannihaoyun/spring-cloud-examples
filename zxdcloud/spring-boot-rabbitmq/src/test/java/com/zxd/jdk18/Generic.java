package com.zxd.jdk18;

/**
 * Created by zxd on 2019/7/31.
 */
public class Generic<T> {

    private T key;

    public Generic(T key) {
        this.key = key;
    }

    public T getKey() {
        return key;
    }
}
