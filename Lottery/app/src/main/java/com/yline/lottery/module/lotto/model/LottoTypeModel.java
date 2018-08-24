package com.yline.lottery.module.lotto.model;

import java.io.Serializable;
import java.util.List;

public class LottoTypeModel implements Serializable {
	private static final long serialVersionUID = -8480510311384257690L;
	
	private String lottery_id; // 彩票ID
	private String lottery_name; // 	彩票名称
	private String lottery_type_id; // 彩票类型，1:福利彩票 2:体育彩票
	private String remarks; // 描述信息 {每周二、四、日开奖}
	
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
	
	public String getLottery_type_id() {
		return lottery_type_id;
	}
	
	public void setLottery_type_id(String lottery_type_id) {
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
