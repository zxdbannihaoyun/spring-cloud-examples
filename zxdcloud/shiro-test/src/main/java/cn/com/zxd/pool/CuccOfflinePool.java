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
 * Created by xuhf on 2018/8/14.
 */
@Component
public class CuccOfflinePool extends BasePool{

    private PoolingHttpClientConnectionManager cm = null;

    private RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
            .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
            .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT).setStaleConnectionCheckEnabled(true)
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

    @Override
    public Client getClient() {
        Client client = new Client();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultRequestConfig(defaultRequestConfig).setRetryHandler(httpRequestRetryHandler).build();
        client.setHttpClient(httpClient);
        client.setRequestConfig(RequestConfig.copy(defaultRequestConfig).build());
        return client;
    }

}
