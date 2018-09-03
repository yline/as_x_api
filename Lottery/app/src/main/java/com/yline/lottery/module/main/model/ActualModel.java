package com.yline.lottery.module.main.model;

import java.io.Serializable;

/**
 * 最新展示的信息
 *
 * @author yline 2018/8/31 -- 16:53
 */
public class ActualModel implements Serializable {
	private static final long serialVersionUID = 5411295556852090892L;
	
	private String lottoId;
	private String result; // "03,08,11,14,18,23,16",
	private String number; // 开奖期号 18028
	private String date; // 开奖日期 "2018-03-13"
	
	public static ActualModel genActualModel(String lottoId, String result, String number, String date) {
		ActualModel actualModel = new ActualModel();
		actualModel.lottoId = lottoId;
		actualModel.result = result;
		actualModel.number = number;
		actualModel.date = date;
		return actualModel;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getLottoId() {
		return lottoId;
	}
	
	public void setLottoId(String lottoId) {
		this.lottoId = lottoId;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
}
