package cn.com.zxd.pool;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by xuhf on 2018/5/23.
 */
public abstract class BasePool {

    public static int DEFAULT_SOCKET_TIMEOUT = 3000;

    public static int DEFAULT_CONNECT_TIMEOUT = 3000;

    public static int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 3000;

    public static int maxConnTotal = 300; // 最大不要超过1000

    public static int maxConnPerRoute = 100; // 实际的单个连接池大小，如tps定为50，那就配置50

    public static HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            return false;
        }
    };

    public abstract Client getClient();


}
