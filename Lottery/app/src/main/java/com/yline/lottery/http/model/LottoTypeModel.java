package com.yline.lottery.http.model;

import java.io.Serializable;

public class LottoTypeModel implements Serializable {
	public static final int TYPE_WELFARE = 1;
	private static final long serialVersionUID = -8480510311384257690L;
	
	private String lottery_id; // 彩票ID
	private String lottery_name; // 	彩票名称
	private int lottery_type_id; // 彩票类型，1:福利彩票 2:体育彩票
	private String remarks; // 描述信息 {每周二、四、日开奖}
	
	public LottoTypeModel() {
	}
	
	public LottoTypeModel(String lottery_id, String lottery_name, int lottery_type_id, String remarks) {
		this.lottery_id = lottery_id;
		this.lottery_name = lottery_name;
		this.lottery_type_id = lottery_type_id;
		this.remarks = remarks;
	}
	
	public String getLottery_id() {
		return lottery_id;
	}
	
	public void setLottery_id(String lottery_id) {
		this.lottery_id = lottery_id;
	}
	
	public String getLottery_name() {
		return lottery_name;
	}
	
	public void setLottery_name(String lottery_name) {
		this.lottery_name = lottery_name;
	}
	
	public int getLottery_type_id() {
		return lottery_type_id;
	}
	
	public void setLottery_type_id(int lottery_type_id) {
		this.lottery_type_id = lottery_type_id;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Override
	public String toString() {
		return "LottoTypeModel{" +
				"lottery_id='" + lottery_id + '\'' +
				", lottery_name='" + lottery_name + '\'' +
				", lottery_type_id='" + lottery_type_id + '\'' +
				", remarks='" + remarks + '\'' +
				'}';
	}
}
