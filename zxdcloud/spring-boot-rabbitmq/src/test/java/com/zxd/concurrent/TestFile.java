package com.zxd.concurrent;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * Created by zxd on 2019/8/2.
 */
public class TestFile {

    @Test
    public void test() throws Exception{
        FileUtils.readLines(new File(""));
    }
}
