package cn.com.zxd.pool;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * 上级服务上的请求
 * 
 * @author xuhf
 *
 */
public class ProviderRequest {

	private String name;

	private String url;

	private Date requestTime;

	private Date responseTime;

	// 上级服务商请求参数
	private JSONObject providerRequestJson;

	// 上级服务商响应
	private JSONObject providerResponseJson;

	private boolean success;

	private String responseCode;

	private String provider;

	/**
	 * http请求状态码
	 */
	private String httpStatus;

	private String httpError;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public JSONObject getProviderRequestJson() {
		return providerRequestJson;
	}

	public void setProviderRequestJson(JSONObject providerRequestJson) {
		this.providerRequestJson = providerRequestJson;
	}

	public JSONObject getProviderResponseJson() {
		return providerResponseJson;
	}

	public void setProviderResponseJson(JSONObject providerResponseJson) {
		this.providerResponseJson = providerResponseJson;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * 返回http请求状态码
	 * 
	 * @return
	 */
	public String getHttpStatus() {
		return httpStatus;
	}

	/**
	 * 设置http请求状态码
	 * 
	 * @param httpStatus
	 */
	public void setHttpStatus(String httpStatus) {
		this.httpStatus = httpStatus;
	}

	/**
	 * 返回http非200状态码错误信息
	 * 
	 * @return
	 */
	public String getHttpError() {
		return httpError;
	}

	/**
	 * 设置http非200状态码错误信息
	 * 
	 * @param httpError
	 */
	public void setHttpError(String httpError) {
		this.httpError = httpError;
	}

}
