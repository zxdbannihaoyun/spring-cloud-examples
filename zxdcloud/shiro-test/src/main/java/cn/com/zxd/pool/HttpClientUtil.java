package cn.com.zxd.pool;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http接口请求
 *
 * @author xuhf
 */
public class HttpClientUtil {


    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static JSONObject doJsonPost(ProviderRequest pr, CloseableHttpClient httpclient, String url, JSONObject json) {
        CloseableHttpResponse r = null;
        HttpEntity entity = null;
        try {
            // 设置参数
            StringEntity s = new StringEntity(json.toJSONString(), CommonConfig.CHARSET);
            s.setContentEncoding(CommonConfig.CHARSET);
            s.setContentType("application/json");
            HttpPost post = new HttpPost(url);
            post.setEntity(s);
            post.setHeader("content-type", "application/json;charset=utf-8");
            r = httpclient.execute(post);
            int statusCode = r.getStatusLine().getStatusCode();
            pr.setHttpStatus(String.valueOf(statusCode));// 设置http返回状态值
            if (HttpStatus.SC_OK == statusCode) {
                entity = r.getEntity();
                String response = EntityUtils.toString(entity, CommonConfig.CHARSET);
                return JSONObject.parseObject(response);
            } else {
                String httpError = "url : " + url + ", json : " + json + ", response statusCode : "
                        + r.getStatusLine().toString();
                pr.setHttpError(httpError);
                logger.error(httpError);
                return null;
            }
        } catch (Exception e) {
            logger.error("url : " + url + ", json : " + json, e);
        } finally {
            try {
                EntityUtils.consume(entity);
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.error("url : " + url + ", json : " + json, e);
            }
        }
        return null;
    }

    public static JSONObject doFormPost(ProviderRequest pr, CloseableHttpClient httpclient, String url,
                                        Map<String, Object> parameters) {
        CloseableHttpResponse r = null;
        HttpEntity entity = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String k : parameters.keySet()) {
            NameValuePair p = new BasicNameValuePair(k, (String) parameters.get(k));
            params.add(p);
        }
        try {
            // 设置参数
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            post.setHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8");
            r = httpclient.execute(post);
            int statusCode = r.getStatusLine().getStatusCode();
            pr.setHttpStatus(String.valueOf(statusCode));// 设置http返回状态值
            if (HttpStatus.SC_OK == statusCode) {
                entity = r.getEntity();
                String response = EntityUtils.toString(entity, CommonConfig.CHARSET);
                return JSONObject.parseObject(response);
            } else {
                String httpError = "url : " + url + ", parameters : " + parameters + ", response statusCode : "
                        + statusCode;
                pr.setHttpError(httpError);
                logger.error(httpError);
                return null;
            }
        } catch (Exception e) {
            logger.error("url : " + url + ", parameters : " + parameters, e);
        } finally {
            try {
                EntityUtils.consume(entity);
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.error("url : " + url + ", parameters : " + parameters, e);
            }
        }
        return null;
    }

    public static JSONObject doGet(ProviderRequest pr, CloseableHttpClient httpclient, String url,
                                   Map<String, Object> parameters) {
        CloseableHttpResponse r = null;
        HttpEntity entity = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String k : parameters.keySet()) {
            NameValuePair p = new BasicNameValuePair(k, (String) parameters.get(k));
            params.add(p);
        }
        try {
            String urlParameters = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            System.out.println(urlParameters);
            HttpGet get = new HttpGet(url + "?" + urlParameters);
            r = httpclient.execute(get);
            int statusCode = r.getStatusLine().getStatusCode();
            pr.setHttpStatus(String.valueOf(statusCode));// 设置http返回状态值
            if (HttpStatus.SC_OK == statusCode) {
                entity = r.getEntity();
                String response = EntityUtils.toString(entity, CommonConfig.CHARSET);
                return JSONObject.parseObject(response);
            } else {
                String httpError = "url : " + url + ", json : " + parameters + ", response statusCode : " + statusCode;
                pr.setHttpError(httpError);
                logger.error(httpError);
                return null;
            }
        } catch (Exception e) {
            logger.error("url : " + url + ", json : " + parameters, e);
        } finally {
            try {
                EntityUtils.consume(entity);
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.error("url : " + url + ", json : " + parameters, e);
            }
        }
        return null;
    }

    public static JSONObject doJsonPost(ProviderRequest pr, Client client, String url, JSONObject json) {
        CloseableHttpResponse r = null;
        HttpEntity entity = null;
        try {
            // 设置参数
            StringEntity s = new StringEntity(json.toJSONString(), CommonConfig.CHARSET);
            s.setContentEncoding(CommonConfig.CHARSET);
            s.setContentType("application/json");
            HttpPost post = new HttpPost(url);
            post.setConfig(client.getRequestConfig());
            post.setEntity(s);
            post.setHeader("content-type", "application/json;charset=utf-8");
            r = client.getHttpClient().execute(post);
            int statusCode = r.getStatusLine().getStatusCode();
            pr.setHttpStatus(String.valueOf(statusCode));// 设置http返回状态值
            if (HttpStatus.SC_OK == statusCode) {
                entity = r.getEntity();
                String response = EntityUtils.toString(entity, CommonConfig.CHARSET);
                return JSONObject.parseObject(response);
            } else {
                String httpError = "url : " + url + ", json : " + json + ", response statusCode : "
                        + r.getStatusLine().toString();
                pr.setHttpError(httpError);
                logger.error(httpError);
                return null;
            }
        } catch (Exception e) {
            logger.error("url : " + url + ", json : " + json, e);
        } finally {
            try {
                EntityUtils.consume(entity);
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.error("url : " + url + ", json : " + json, e);
            }
        }
        return null;
    }

    public static JSONObject doFormPost(ProviderRequest pr, Client client, String url, Map<String, Object> parameters) {
        CloseableHttpResponse r = null;
        HttpEntity entity = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String k : parameters.keySet()) {
            NameValuePair p = new BasicNameValuePair(k, (String) parameters.get(k));
            params.add(p);
        }
        try {
            // 设置参数
            HttpPost post = new HttpPost(url);
            post.setConfig(client.getRequestConfig());
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            post.setHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8");
            r = client.getHttpClient().execute(post);
            int statusCode = r.getStatusLine().getStatusCode();
            pr.setHttpStatus(String.valueOf(statusCode));// 设置http返回状态值
            if (HttpStatus.SC_OK == statusCode) {
                entity = r.getEntity();
                String response = EntityUtils.toString(entity, CommonConfig.CHARSET);
                return JSONObject.parseObject(response);
            } else {
                String httpError = "url : " + url + ", parameters : " + parameters + ", response statusCode : "
                        + statusCode;
                pr.setHttpError(httpError);
                logger.error(httpError);
                return null;
            }
        } catch (Exception e) {
            logger.error("url : " + url + ", parameters : " + parameters, e);
        } finally {
            try {
                EntityUtils.consume(entity);
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.error("url : " + url + ", parameters : " + parameters, e);
            }
        }
        return null;
    }

    public static JSONObject doFormPostCustom(ProviderRequest pr, Client client, String url, Map<String, String> parameters) {
        CloseableHttpResponse r = null;
        HttpEntity entity = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String k : parameters.keySet()) {
            NameValuePair p = new BasicNameValuePair(k, (String) parameters.get(k));
            params.add(p);
        }
        try {
            // 设置参数
            HttpPost post = new HttpPost(url);
            post.setConfig(client.getRequestConfig());
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            post.setHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8");
            r = client.getHttpClient().execute(post);
            int statusCode = r.getStatusLine().getStatusCode();
            pr.setHttpStatus(String.valueOf(statusCode));// 设置http返回状态值
            if (HttpStatus.SC_OK == statusCode) {
                entity = r.getEntity();
                String response = EntityUtils.toString(entity, CommonConfig.CHARSET);
                return JSONObject.parseObject(response);
            } else {
                String httpError = "url : " + url + ", parameters : " + parameters + ", response statusCode : "
                        + statusCode;
                pr.setHttpError(httpError);
                logger.error(httpError);
                return null;
            }
        } catch (Exception e) {
            logger.error("url : " + url + ", parameters : " + parameters, e);
        } finally {
            try {
                EntityUtils.consume(entity);
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.error("url : " + url + ", parameters : " + parameters, e);
            }
        }
        return null;
    }

    public static JSONObject doGet(ProviderRequest pr, Client client, String url, Map<String, Object> parameters) {
        CloseableHttpResponse r = null;
        HttpEntity entity = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String k : parameters.keySet()) {
            NameValuePair p = new BasicNameValuePair(k, (String) parameters.get(k));
            params.add(p);
        }
        try {
            String urlParameters = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            HttpGet get = new HttpGet(url + "?" + urlParameters);
            get.setConfig(client.getRequestConfig());
            r = client.getHttpClient().execute(get);
            int statusCode = r.getStatusLine().getStatusCode();
            pr.setHttpStatus(String.valueOf(statusCode));// 设置http返回状态值
            if (HttpStatus.SC_OK == statusCode) {
                entity = r.getEntity();
                String response = EntityUtils.toString(entity, CommonConfig.CHARSET);
                return JSONObject.parseObject(response);
            } else {
                String httpError = "url : " + url + ", json : " + parameters + ", response statusCode : " + statusCode;
                pr.setHttpError(httpError);
                logger.error(httpError);
                return null;
            }
        } catch (Exception e) {
            logger.error("url : " + url + ", json : " + parameters, e);
        } finally {
            try {
                EntityUtils.consume(entity);
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.error("url : " + url + ", json : " + parameters, e);
            }
        }
        return null;
    }

    /**
     * JSON格式请求
     *
     * @param Client
     * @param url
     * @param json
     * @return
     */
    public static JSONObject doPostJson(Client httpClient, String url, JSONObject json) {
        CloseableHttpResponse r = null;
        HttpEntity entity = null;
        try {
            // 设置参数
            StringEntity s = new StringEntity(json.toJSONString(), CommonConfig.CHARSET);
            s.setContentEncoding(CommonConfig.CHARSET);
            s.setContentType("application/json");
            HttpPost post = new HttpPost(url);
            post.setEntity(s);
            post.setHeader("content-type", "application/json;CHARSET=UTF-8");

            r = httpClient.getHttpClient().execute(post);
            int statusCode = r.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                entity = r.getEntity();
                String response = EntityUtils.toString(entity, CommonConfig.CHARSET);
                return JSONObject.parseObject(response);
            } else {
                logger.error("url : " + url + ", parameters : " + json + ", response statusCode : " + statusCode);
                return null;
            }
        } catch (Exception e) {
            logger.error("url : " + url + ", parameters : " + json, e);
        } finally {
            try {
                EntityUtils.consume(entity);
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.error("url : " + url + ", parameters : " + json, e);
            }
        }
        return null;
    }

    /**
     * 九州数据特定请求头
     *
     * @param pr
     * @param client
     * @param url
     * @param json
     * @return
     */
    public static JSONObject doPostJson(ProviderRequest pr, Client client, String url, JSONObject json) {
        CloseableHttpResponse r = null;
        HttpEntity entity = null;
        try {
            // 设置参数
            String token = json.getString("token");
            StringEntity s = new StringEntity(json.toJSONString(), CommonConfig.CHARSET);
            s.setContentEncoding(CommonConfig.CHARSET);
            s.setContentType("application/json");
            HttpPost post = new HttpPost(url);
            post.setConfig(client.getRequestConfig());
            post.setEntity(s);
            post.setHeader("content-type", "application/json;charset=utf-8");
            post.setHeader("Authorization", token);
            r = client.getHttpClient().execute(post);
            int statusCode = r.getStatusLine().getStatusCode();
            pr.setHttpStatus(String.valueOf(statusCode));// 设置http返回状态值
            if (HttpStatus.SC_OK == statusCode) {
                entity = r.getEntity();
                String response = EntityUtils.toString(entity, CommonConfig.CHARSET);
                return JSONObject.parseObject(response);
            } else {
                String httpError = "url : " + url + ", json : " + json + ", response statusCode : "
                        + r.getStatusLine().toString();
                pr.setHttpError(httpError);
                logger.error(httpError);
                return null;
            }
        } catch (Exception e) {
            logger.error("url : " + url + ", json : " + json, e);
        } finally {
            try {
                EntityUtils.consume(entity);
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                logger.error("url : " + url + ", json : " + json, e);
            }
        }
        return null;
    }

}
