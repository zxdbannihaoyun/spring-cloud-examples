package com.zxd.jdk18;

import org.jboss.logging.Logger;
import org.junit.Test;

/**
 * Created by zxd on 2019/7/31.
 */
public class TestGeneric {

    Logger logger = Logger.getLogger(TestGeneric.class);

    @Test
    public void test() {
        Generic<Number> g1 = new Generic<Number>(123);
        Generic<Integer> g2 = new Generic<Integer>(456);
        showKeyValue(g1);
        showKeyValue1(g2);


    }

    private void showKeyValue(Generic<Number> obj) {
        logger.info("泛型测试：" + obj.getKey());
    }

    private void showKeyValue1(Generic<?> obj) {
        logger.info("泛型测试：" + obj.getKey());
    }
}
