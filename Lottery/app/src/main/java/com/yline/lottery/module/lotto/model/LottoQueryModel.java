package com.yline.lottery.module.lotto.model;

import java.io.Serializable;
import java.util.List;

public class LottoQueryModel implements Serializable {
	private static final long serialVersionUID = -6671306971328019887L;
	
	private String lottery_id; // 彩票种类 id
	private String lottery_name; // "双色球",
	private String lottery_res; // "03,08,11,14,18,23,16",
	private String lottery_no; //  开奖期号 18028
	private String lottery_date; // 	开奖日期 "2018-03-13",
	private String lottery_exdate; // 兑奖截止日期 "2018-05-11",
	private String lottery_sale_amount; // 	本期销售额，可能为空 "352,015,830",
	private String lottery_pool_amount; // 	奖池滚存，可能为空  "578,094,167",
	
	private List<LottoQueryDetailModel> lottery_prize;  // 开奖详情，可能为null
	
	public static class LottoQueryDetailModel implements Serializable {
		private static final long serialVersionUID = -818954397128537626L;
		
		private String prize_name; // 奖项名称
		private int prize_num; // 中奖数量，公布数据可能延时可能为空或--
		private String prize_amount; // 中奖金额 "7,676,997",
		private String prize_require; // 	中奖条件 "6+1"
		
		public String getPrize_name() {
			return prize_name;
		}
		
		public void setPrize_name(String prize_name) {
			this.prize_name = prize_name;
		}
		
		public int getPrize_num() {
			return prize_num;
		}
		
		public void setPrize_num(int prize_num) {
			this.prize_num = prize_num;
		}
		
		public String getPrize_amount() {
			return prize_amount;
		}
		
		public void setPrize_amount(String prize_amount) {
			this.prize_amount = prize_amount;
		}
		
		public String getPrize_require() {
			return prize_require;
		}
		
		public void setPrize_require(String prize_require) {
			this.prize_require = prize_require;
		}
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
	
	public String getLottery_res() {
		return lottery_res;
	}
	
	public void setLottery_res(String lottery_res) {
		this.lottery_res = lottery_res;
	}
	
	public String getLottery_no() {
		return lottery_no;
	}
	
	public void setLottery_no(String lottery_no) {
		this.lottery_no = lottery_no;
	}
	
	public String getLottery_date() {
		return lottery_date;
	}
	
	public void setLottery_date(String lottery_date) {
		this.lottery_date = lottery_date;
	}
	
	public String getLottery_exdate() {
		return lottery_exdate;
	}
	
	public void setLottery_exdate(String lottery_exdate) {
		this.lottery_exdate = lottery_exdate;
	}
	
	public String getLottery_sale_amount() {
		return lottery_sale_amount;
	}
	
	public void setLottery_sale_amount(String lottery_sale_amount) {
		this.lottery_sale_amount = lottery_sale_amount;
	}
	
	public String getLottery_pool_amount() {
		return lottery_pool_amount;
	}
	
	public void setLottery_pool_amount(String lottery_pool_amount) {
		this.lottery_pool_amount = lottery_pool_amount;
	}
	
	public List<LottoQueryDetailModel> getLottery_prize() {
		return lottery_prize;
	}
	
	public void setLottery_prize(List<LottoQueryDetailModel> lottery_prize) {
		this.lottery_prize = lottery_prize;
	}
}
