package cn.com.zxd.pool;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by xuhf on 2018/5/23.
 */
@Component
public class QyPool extends BasePool {

    public static int SOCKET_TIMEOUT = 3000;

    public static int CONNECT_TIMEOUT = 3000;

    public static int CONNECTION_REQUEST_TIMEOUT = 3000;

    private PoolingHttpClientConnectionManager cm = null;

    // 设置超时
    // http://gaozzsoft.iteye.com/blog/2352241
    // setConnectTimeout：设置连接超时时间，单位毫秒。
    // setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
    // setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
    @SuppressWarnings("deprecation")
    private RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setStaleConnectionCheckEnabled(true)
            .build();

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
        cm.setMaxTotal(maxConnTotal);
        // 将每个路由基础的连接增加到20
        cm.setDefaultMaxPerRoute(maxConnPerRoute);
    }

    public Client getClient() {
        Client client = new Client();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultRequestConfig(defaultRequestConfig).setRetryHandler(httpRequestRetryHandler).build();
        client.setHttpClient(httpClient);
        client.setRequestConfig(RequestConfig.copy(defaultRequestConfig).build());
        return client;
    }

    public PoolingHttpClientConnectionManager getManager() {
        return cm;
    }

}
