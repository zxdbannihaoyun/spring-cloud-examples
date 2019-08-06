package cn.com.zxd.pool;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * HttpClient连接池
 *
 * @author xuhf
 */
@Component
public class HttpClientPool {

    private PoolingHttpClientConnectionManager cm = null;

    // 30秒超时
    private static int SOCKET_TIMEOUT = 30000;

    private static int CONNECT_TIMEOUT = 30000;

    private static int CONNECTION_REQUEST_TIMEOUT = 30000;

    // 默认的超时时间都是3秒
    private static int DEFAULT_SOCKET_TIMEOUT = 3000;

    private static int DEFAULT_CONNECT_TIMEOUT = 3000;

    private static int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 3000;

    @PostConstruct
    public void init() {
        // https://my.oschina.net/wenziqiu/blog/339630
        // 信任所有的https链接
        PlainConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        // https信任所有证书
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws CertificateException {
                    return true;
                }
            }).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf).register("https", sslsf).build();
        cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加到200
        cm.setMaxTotal(200);
        // 将每个路由基础的连接增加到100
        cm.setDefaultMaxPerRoute(100);
    }

    /**
     * 获取30秒超时的Http连接
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultRequestConfig(getRequestConfig()).setRetryHandler(getRetryHandler()).build();
        return httpClient;
    }

    /**
     * 获取3秒超时的Http连接
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public CloseableHttpClient getDefaultHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultRequestConfig(getDefaultRequestConfig()).setRetryHandler(getRetryHandler()).build();
        return httpClient;
    }

    /**
     * 重试策略
     *
     * @return
     */
    private HttpRequestRetryHandler getRetryHandler() {
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        return httpRequestRetryHandler;
    }

    @SuppressWarnings("deprecation")
    private RequestConfig getRequestConfig() {
        // 设置超时时间为30秒
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setStaleConnectionCheckEnabled(true).build();
        return requestConfig;
    }

    @SuppressWarnings("deprecation")
    private RequestConfig getDefaultRequestConfig() {
        // 设置超时时间为3秒
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                .setStaleConnectionCheckEnabled(true).build();
        return defaultRequestConfig;
    }

}
