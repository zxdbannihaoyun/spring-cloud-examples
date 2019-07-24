package com.zxd.thread;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zxd on 2019/5/8.
 */
public class MyFileUtil {

    private static MyFileUtil myFileUtil = new MyFileUtil();
    private static MyFileUtil instance = null;

    private MyFileUtil() {
        throw new AssertionError();
    }

    public static MyFileUtil getMyFileUtil() {
        return myFileUtil;
    }

    public static synchronized MyFileUtil getInstance() {
        if (instance == null)
            instance = new MyFileUtil();
        return instance;
    }


    public static void fileCopy(String source, String target) throws IOException {
        try (InputStream in = new FileInputStream(source)) {
            try (OutputStream out = new FileOutputStream(target)) {
                byte[] buffer = new byte[4096];
                int bytesToRead;
                while ((bytesToRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesToRead);
                }
            }
        }
    }

    public static void fileCopyNIO(String source, String target) throws IOException {
        try (FileInputStream in = new FileInputStream(source)) {
            try (FileOutputStream out = new FileOutputStream(target)) {
                FileChannel inChannel = in.getChannel();
                FileChannel outChannel = out.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                while (inChannel.read(buffer) != -1) {
                    buffer.flip();
                    outChannel.write(buffer);
                    buffer.clear();
                }
            }
        }
    }

}
