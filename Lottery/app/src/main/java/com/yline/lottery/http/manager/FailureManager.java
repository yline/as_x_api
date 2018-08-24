package com.yline.lottery.http.manager;

import android.accounts.NetworkErrorException;
import android.text.TextUtils;

import com.yline.application.SDKManager;
import com.yline.http.OkHttpUtils;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLException;

public class FailureManager {
	/**
	 * 异常信息处理（沿袭之前的方案）
	 *
	 * @param ex   异常
	 * @param code 错误码 -100{IO异常}，-200{Json解析异常}，其它{服务器返回异常}
	 * @param msg  错误信息
	 */
	public static String failureException(final Exception ex, int code, String msg) {
		if (!TextUtils.isEmpty(msg)) {
			SDKManager.toast("请求失败:" + msg);
		}
		
		// 后台返回的异常信息
		if (code != OkHttpUtils.IO_ERROR_CODE && code != OkHttpUtils.HANDLE_ERROR_CODE) {
			return msg;
		}
		
		// 本地设置的异常信息
		if (ex instanceof TimeoutException || ex instanceof SocketTimeoutException) {
			return "连接超时";
		} else if (ex instanceof JSONException || ex instanceof XmlPullParserException || ex instanceof ParseException) {
			return "数据解析异常";
		} else if (ex instanceof NetworkErrorException) {
			return "网络异常";
		} else if (ex instanceof SSLException) {
			return "证书出错";
		} else if (ex instanceof ConnectException || ex instanceof UnknownHostException) {
			return "无网络,请重试!";
		} else {
			return "系统异常";
		}
	}
}
