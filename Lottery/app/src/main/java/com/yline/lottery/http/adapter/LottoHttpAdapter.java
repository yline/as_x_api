package com.yline.lottery.http.adapter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yline.http.OkHttpUtils;
import com.yline.http.adapter.OnHttpAdapter;
import com.yline.http.adapter.helper.ClientHelper;
import com.yline.http.callback.OnParseCallback;
import com.yline.http.json.FastJson;
import com.yline.lottery.http.manager.DefaultClientHelper;
import com.yline.lottery.http.manager.FailureManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LottoHttpAdapter implements OnHttpAdapter {
	private static final String LottoKey = "1c5837d3671a52d0494ec6670441f86a";
	
	private String httpUrl;
	private Map<String, String> paramMap;
	private Object tag;
	
	public LottoHttpAdapter(String httpUrl, Map<String, String> param, Object tag) {
		this.httpUrl = httpUrl;
		this.paramMap = param;
		this.tag = tag;
	}
	
	@Override
	public OkHttpClient getHttpClient() {
		return DefaultClientHelper.getDefaultHttpClient();
	}
	
	@Override
	public Request getHttpRequest() {
		return buildPostRequest(httpUrl, paramMap, tag);
	}
	
	@Override
	public <T> void handleResponse(Response response, Type type, OnParseCallback<T> callback) throws IOException {
		if (response.code() == 200) {
			ResponseBody responseBody = response.body();
			if (null != responseBody) {
				try {
					String bodyString = responseBody.string();
					LottoBaseModel<JSON> baseModel = FastJson.toClass(bodyString, new TypeReference<LottoBaseModel<JSON>>() {}.getType());
					
					int code = baseModel.getError_code();
					if (code == 0) {
						JSON jsonData = baseModel.getResult();
						if (null != jsonData) {
							T data = jsonData.toJavaObject(type);
							callback.onResponseSuccess(data);
						} else {
							callback.onResponseSuccess(null);
						}
					} else {
						callback.onResponseError(new IOException("code error"), code, baseModel.getReason());
					}
				} catch (IOException ex) {
					callback.onResponseError(ex, OkHttpUtils.HANDLE_ERROR_CODE, null);
				}
			} else {
				callback.onResponseError(new IOException("response body is null"), OkHttpUtils.HANDLE_ERROR_CODE, null);
			}
		} else {
			callback.onResponseError(new IOException("response code error"), response.code(), null);
		}
	}
	
	@Override
	public String handleFailureException(Exception ex, int code, String msg) {
		return FailureManager.failureException(ex, code, msg);
	}
	
	/**
	 * 构建 Post请求的Request；
	 * 使用body{json}方式传播
	 *
	 * @param httpUrl 请求链接
	 * @param params  请求参数
	 * @param tag     返回的tag
	 * @return Request对象
	 */
	private static Request buildPostRequest(String httpUrl, Map<String, String> params, Object tag) {
		Request.Builder requestBuilder = new Request.Builder();
		requestBuilder.url(httpUrl);
		
		// 添加Http请求，协议的头部
		requestBuilder.addHeader("Content-Type", "text/json; charset=UTF-8");
		
		// body信息
		MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
		multiBuilder.setType(MultipartBody.FORM);
		buildParamBuilder(multiBuilder);
		if (null != params) {
			for (String key : params.keySet()) {
				String value = params.get(key);
				if (!TextUtils.isEmpty(value)) {
					multiBuilder.addFormDataPart(key, value);
				}
			}
		}
		requestBuilder.post(multiBuilder.build());
		// tag
		requestBuilder.tag(tag);
		
		// 返回结果
		return requestBuilder.build();
	}
	
	private static void buildParamBuilder(MultipartBody.Builder multiBuilder) {
		multiBuilder.addFormDataPart("key", LottoKey);
	}
}
