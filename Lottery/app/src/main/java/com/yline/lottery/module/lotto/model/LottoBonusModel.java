package com.yline.lottery.module.lotto.model;

import java.io.Serializable;
import java.util.List;

public class LottoBonusModel implements Serializable {
	private static final long serialVersionUID = -4336838646658046907L;
	
	private String lottery_id; // 彩票种类 id
	private String lottery_name; // "双色球",
	private String lottery_no; // 18028 	开奖期号
	private String lottery_date; // 	开奖日期 "2018-03-13",
	
	private String real_lottery_res; // 	实际开奖号码 "01,02,09,14,22,25,05",
	private String lottery_res; // 	投注的号码 "01,11,02,09,14,22,25|05,03",
	private int in_money; // 投注金额 - 28
	private String buy_red_ball_num; // 	投注红色球数目
	private String buy_blue_ball_num; // 投注蓝色球数目
	private String hit_red_ball_num; // 	命中红色球数目
	private String hit_blue_ball_num; // 	命中蓝色球数目
	private int is_prize; // 是否中奖，1:中奖 2:未中奖
	private String prize_msg; // 	是否中奖描述
	
	private List<LottoBonusDetail> lottery_prize; // 中奖清单，未中奖则为null
	
	public static class LottoBonusDetail implements Serializable {
		private static final long serialVersionUID = -3725074241264205292L;
		
		private String prize_name; // 奖项名称
		private String prize_require; // 	中奖条件 "6+1"
		private int prize_num; // 中奖数量，公布数据可能延时可能为空或--
		private String prize_money; // 单注奖金 "7,676,997",
		
		public String getPrize_name() {
			return prize_name;
		}
		
		public void setPrize_name(String prize_name) {
			this.prize_name = prize_name;
		}
		
		public String getPrize_require() {
			return prize_require;
		}
		
		public void setPrize_require(String prize_require) {
			this.prize_require = prize_require;
		}
		
		public int getPrize_num() {
			return prize_num;
		}
		
		public void setPrize_num(int prize_num) {
			this.prize_num = prize_num;
		}
		
		public String getPrize_money() {
			return prize_money;
		}
		
		public void setPrize_money(String prize_money) {
			this.prize_money = prize_money;
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
	
	public String getReal_lottery_res() {
		return real_lottery_res;
	}
	
	public void setReal_lottery_res(String real_lottery_res) {
		this.real_lottery_res = real_lottery_res;
	}
	
	public String getLottery_res() {
		return lottery_res;
	}
	
	public void setLottery_res(String lottery_res) {
		this.lottery_res = lottery_res;
	}
	
	public int getIn_money() {
		return in_money;
	}
	
	public void setIn_money(int in_money) {
		this.in_money = in_money;
	}
	
	public String getBuy_red_ball_num() {
		return buy_red_ball_num;
	}
	
	public void setBuy_red_ball_num(String buy_red_ball_num) {
		this.buy_red_ball_num = buy_red_ball_num;
	}
	
	public String getBuy_blue_ball_num() {
		return buy_blue_ball_num;
	}
	
	public void setBuy_blue_ball_num(String buy_blue_ball_num) {
		this.buy_blue_ball_num = buy_blue_ball_num;
	}
	
	public String getHit_red_ball_num() {
		return hit_red_ball_num;
	}
	
	public void setHit_red_ball_num(String hit_red_ball_num) {
		this.hit_red_ball_num = hit_red_ball_num;
	}
	
	public String getHit_blue_ball_num() {
		return hit_blue_ball_num;
	}
	
	public void setHit_blue_ball_num(String hit_blue_ball_num) {
		this.hit_blue_ball_num = hit_blue_ball_num;
	}
	
	public int getIs_prize() {
		return is_prize;
	}
	
	public void setIs_prize(int is_prize) {
		this.is_prize = is_prize;
	}
	
	public String getPrize_msg() {
		return prize_msg;
	}
	
	public void setPrize_msg(String prize_msg) {
		this.prize_msg = prize_msg;
	}
	
	public List<LottoBonusDetail> getLottery_prize() {
		return lottery_prize;
	}
	
	public void setLottery_prize(List<LottoBonusDetail> lottery_prize) {
		this.lottery_prize = lottery_prize;
	}
}
