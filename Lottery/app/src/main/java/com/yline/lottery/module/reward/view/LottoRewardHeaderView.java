package com.yline.lottery.module.reward.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.yline.lottery.R;
import com.yline.view.recycler.holder.ViewHolder;

public class LottoRewardHeaderView extends RelativeLayout {
	private ViewHolder mViewHolder;
	
	public LottoRewardHeaderView(Context context) {
		this(context, null);
	}
	
	public LottoRewardHeaderView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public LottoRewardHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.view_lotto_reward_header, this, true);
		
		initView();
	}
	
	private void initView() {
		mViewHolder = new ViewHolder(this);
		
		initViewClick();
	}
	
	private void initViewClick() {
	
	}
	
	/**
	 * 修改头部数据
	 *
	 * @param name         "双色球"
	 * @param term         开奖期号 18028
	 * @param date         开奖日期 "2018-03-13",
	 * @param exchangeDate 兑奖截止日期 "2018-05-11",
	 * @param result       "03,08,11,14,18,23,16",
	 * @param sale         销售额
	 * @param pool         奖池
	 */
	public void setData(String name, String term, String date, String exchangeDate, String result, String sale, String pool) {
		mViewHolder.setText(R.id.view_lotto_reward_header_name, name);
		mViewHolder.setText(R.id.view_lotto_reward_header_term, "第" + term + "期");
		mViewHolder.setText(R.id.view_lotto_reward_header_date, "开奖日期：" + date);
		mViewHolder.setText(R.id.view_lotto_reward_header_date_exchange, "兑奖日期：" + exchangeDate);
		mViewHolder.setText(R.id.view_lotto_reward_header_result, "开奖号码：" + result);
		mViewHolder.setText(R.id.view_lotto_reward_header_sale, "销售额：" + sale + "元");
		mViewHolder.setText(R.id.view_lotto_reward_header_pool, "奖池：" + pool + "元");
	}
}
