package com.yline.lottery.module.lotto.model;

import java.io.Serializable;

public class LottoHistoryModel implements Serializable {
	private static final long serialVersionUID = -2326505114769867123L;
	
	private int page; // 	当前页数 - 1
	private int pageSize; // 每页返回条数 - 5
	private int totalPage; // 总页数 - 447
	
	public class HistoryDetail implements Serializable {
		private static final long serialVersionUID = 8277399259712151099L;
		
		private String lottery_id; // 彩票种类 id
		private String lottery_res; // "03,08,11,14,18,23,16",
		private String lottery_no; // 18028 	开奖期号
		private String lottery_date; // 	开奖日期 "2018-03-13",
		private String lottery_exdate; // 兑奖截止日期 "2018-05-11",
		private String lottery_sale_amount; // 	本期销售额，可能为空 "352,015,830",
		private String lottery_pool_amount; // 	奖池滚存，可能为空  "578,094,167",
		
		public String getLottery_id() {
			return lottery_id;
		}
		
		public void setLottery_id(String lottery_id) {
			this.lottery_id = lottery_id;
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
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getTotalPage() {
		return totalPage;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
