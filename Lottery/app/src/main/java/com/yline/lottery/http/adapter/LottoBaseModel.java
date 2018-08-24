package com.yline.lottery.http.adapter;

import java.io.Serializable;

public class LottoBaseModel<T> implements Serializable {
	private static final long serialVersionUID = -5732944654238806358L;
	
	private String reason; // 结果
	private int error_code; // 错误码
	private T result; // 具体数据
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public int getError_code() {
		return error_code;
	}
	
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	
	public T getResult() {
		return result;
	}
	
	public void setResult(T result) {
		this.result = result;
	}
}
