package cn.com.zxd.pool;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;


public class HttpClientParamTest {
	
	public static void main(String[] args) {
		/**
		 * 创建连接管理器，并设置相关参数
		 */
		//连接管理器，使用无惨构造
		PoolingHttpClientConnectionManager connManager 
		                            = new PoolingHttpClientConnectionManager();
		
		/**
		 * 连接数相关设置
		 */
		//最大连接数
		connManager.setMaxTotal(200); 
		//默认的每个路由的最大连接数
		connManager.setDefaultMaxPerRoute(100); 
		//设置到某个路由的最大连接数，会覆盖defaultMaxPerRoute
		connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 150); 
		
		/**
		 * socket配置（默认配置 和 某个host的配置）
		 */
		SocketConfig socketConfig = SocketConfig.custom()
				.setTcpNoDelay(true)     //是否立即发送数据，设置为true会关闭Socket缓冲，默认为false
				.setSoReuseAddress(true) //是否可以在一个进程关闭Socket后，即使它还没有释放端口，其它进程还可以立即重用端口
				.setSoTimeout(500)       //接收数据的等待超时时间，单位ms
				.setSoLinger(60)         //关闭Socket时，要么发送完所有数据，要么等待60s后，就关闭连接，此时socket.close()是阻塞的
	            .setSoKeepAlive(true)    //开启监视TCP连接是否有效
	            .build();
		connManager.setDefaultSocketConfig(socketConfig);
		connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);
		
		/**
		 * HTTP connection相关配置（默认配置 和 某个host的配置）
		 * 一般不修改HTTP connection相关配置，故不设置
		 */
		//消息约束
		MessageConstraints messageConstraints = MessageConstraints.custom()
	            .setMaxHeaderCount(200)
	            .setMaxLineLength(2000)
	            .build();
		//Http connection相关配置
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
	            .setMalformedInputAction(CodingErrorAction.IGNORE)
	            .setUnmappableInputAction(CodingErrorAction.IGNORE)
	            .setCharset(Consts.UTF_8)
	            .setMessageConstraints(messageConstraints)
	            .build();
		//一般不修改HTTP connection相关配置，故不设置
		//connManager.setDefaultConnectionConfig(connectionConfig);
        //connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);
		
        /**
         * request请求相关配置
         */
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setConnectTimeout(2 * 1000)         //连接超时时间
                .setSocketTimeout(2 * 1000)          //读超时时间（等待数据超时时间）
                .setConnectionRequestTimeout(500)    //从池中获取连接超时时间
                .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接，默认为true，类似testOnBorrow
                .build();
		
		/**
		 * 重试处理
		 * 默认是重试3次
		 */
		//禁用重试(参数：retryCount、requestSentRetryEnabled)
		HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);
		//自定义重试策略
		HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
 
		    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		    	//Do not retry if over max retry count
		        if (executionCount >= 3) {
		            return false;
		        }
		        //Timeout
		        if (exception instanceof InterruptedIOException) {
		            return false;
		        }
		        //Unknown host
		        if (exception instanceof UnknownHostException) {
		            return false;
		        }
		        //Connection refused
		        if (exception instanceof ConnectTimeoutException) {
		            return false;
		        }
		        //SSL handshake exception
		        if (exception instanceof SSLException) {
		            return false;
		        }
		        
		        HttpClientContext clientContext = HttpClientContext.adapt(context);
		        HttpRequest request = clientContext.getRequest();
		        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
		        //Retry if the request is considered idempotent
		        //如果请求类型不是HttpEntityEnclosingRequest，被认为是幂等的，那么就重试
		        //HttpEntityEnclosingRequest指的是有请求体的request，比HttpRequest多一个Entity属性
		        //而常用的GET请求是没有请求体的，POST、PUT都是有请求体的
		        //Rest一般用GET请求获取数据，故幂等，POST用于新增数据，故不幂等
		        if (idempotent) {
		            return true;
		        }
		        
		        return false;
		    }
		};
		
		/**
		 * 创建httpClient
		 */
		CloseableHttpClient httpclient = HttpClients.custom()
	            .setConnectionManager(connManager)             //连接管理器
	            .setProxy(new HttpHost("myproxy", 8080))       //设置代理
	            .setDefaultRequestConfig(defaultRequestConfig) //默认请求配置
	            .setRetryHandler(myRetryHandler)               //重试策略
	            .build();
		
		//创建一个Get请求，并重新设置请求参数，覆盖默认
		HttpGet httpget = new HttpGet("http://www.somehost.com/");
        RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
            .setSocketTimeout(5000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .setProxy(new HttpHost("myotherproxy", 8080))
            .build();
        httpget.setConfig(requestConfig);
        
        CloseableHttpResponse response = null;
        try {
        	//执行请求
			response = httpclient.execute(httpget);
			
			HttpEntity entity = response.getEntity();
			
			// If the response does not enclose an entity, there is no need
            // to bother about connection release
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    instream.read();
                    // do something useful with the response
                } 
                catch (IOException ex) {
                    // In case of an IOException the connection will be released
                    // back to the connection manager automatically
                    throw ex;
                } 
                finally {
                    // Closing the input stream will trigger connection release
                	// 释放连接回到连接池
                    instream.close();
                }
            }
		} 
        catch (Exception e) {
			e.printStackTrace();
		} 
        finally{
        	if(response != null){
        		try {
        			//关闭连接(如果已经释放连接回连接池，则什么也不做)
    				response.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}
        	
        	if(httpclient != null){
        		try {
        			//关闭连接管理器，并会关闭其管理的连接
        			httpclient.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
	}


}
