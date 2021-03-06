package com.yline.lottery.module.reward;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yline.base.BaseActivity;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoQueryModel;
import com.yline.lottery.module.reward.view.LottoRewardHeaderView;
import com.yline.lottery.module.rule.LottoRuleActivity;
import com.yline.lottery.view.LoadingView;
import com.yline.utils.UIScreenUtil;
import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

/**
 * 获奖详情页面
 *
 * @author yline 2018/9/3 -- 18:11
 */
public class LottoRewardActivity extends BaseActivity {
	private static final String LOTTO_ID = "lottery_id"; // 彩票种类
	private static final String LOTTO_NAME = "lottery_name"; // 彩票名称
	private static final String LOTTO_NUMBER = "lottery_number"; // 彩票期号
	
	/**
	 * @param id     彩票种类
	 * @param number 彩票期号
	 */
	public static void launch(Context context, String id, String number, String lottoName) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, LottoRewardActivity.class);
			intent.putExtra(LOTTO_ID, id);
			intent.putExtra(LOTTO_NAME, lottoName);
			intent.putExtra(LOTTO_NUMBER, number);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private String mLottoId;
	private String mLottoNumber;
	private String mLottoName;
	
	private LoadingView mLoadingView;
	private LottoRewardRecyclerAdapter mRecyclerAdapter;
	private LottoRewardHeaderView mHeaderView;
	private TextView titleTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lotto_reward);
		
		Intent intent = getIntent();
		if (null != intent) {
			mLottoId = intent.getStringExtra(LOTTO_ID);
			mLottoName = intent.getStringExtra(LOTTO_NAME);
			mLottoNumber = intent.getStringExtra(LOTTO_NUMBER);
		}
		
		initView();
		initData();
	}
	
	private void initView() {
		mLoadingView = findViewById(R.id.lotto_reward_loading);
		titleTextView = findViewById(R.id.reward_title_content);
		
		mRecyclerAdapter = new LottoRewardRecyclerAdapter(this);
		RecyclerView recyclerView = findViewById(R.id.lotto_reward_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(mRecyclerAdapter);
		
		mHeaderView = new LottoRewardHeaderView(this);
		mRecyclerAdapter.addHeadView(mHeaderView);
		
		// 设置至少距离底部20dp
		View footView = new View(this);
		footView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.dp2px(this, 20)));
		mRecyclerAdapter.addFootView(footView);
		
		initViewClick();
	}
	
	private void initViewClick() {
		// 返回
		findViewById(R.id.reward_title_img).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// 查看规则
		findViewById(R.id.reward_title_rule).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoRuleActivity.launch(LottoRewardActivity.this, mLottoId);
			}
		});
		
		mLoadingView.setOnReloadClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initData();
			}
		});
	}
	
	private void initData() {
		titleTextView.setText(String.format("%s—第%s期", mLottoName, mLottoNumber));
		
		mLoadingView.loading();
		OkHttpManager.lottoQuery(mLottoId, mLottoNumber, new OnJsonCallback<LottoQueryModel>() {
			@Override
			public void onFailure(int code, String msg) {
				mLoadingView.loadFailed();
			}
			
			@Override
			public void onResponse(LottoQueryModel lottoQueryModel) {
				if (null != lottoQueryModel) {
					mLoadingView.loadSuccess();
					
					mHeaderView.setData(lottoQueryModel.getLottery_id(), lottoQueryModel.getLottery_date(), lottoQueryModel.getLottery_exdate(),
							lottoQueryModel.getLottery_res(), lottoQueryModel.getLottery_sale_amount(), lottoQueryModel.getLottery_pool_amount());
					mRecyclerAdapter.setDataList(lottoQueryModel.getLottery_prize(), true);
				} else {
					mLoadingView.loadFailed();
				}
			}
		});
	}
	
	private class LottoRewardRecyclerAdapter extends AbstractHeadFootRecyclerAdapter<LottoQueryModel.LottoQueryDetailModel> {
		private final int normalColor;
		private final int specialColor;
		
		private LottoRewardRecyclerAdapter(Context context) {
			super(context);
			normalColor = ContextCompat.getColor(context, R.color.gray_77);
			specialColor = ContextCompat.getColor(context, R.color.blue_36d);
		}
		
		@Override
		public int getItemRes() {
			return R.layout.item_lotto_reward;
		}
		
		@Override
		public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
			LottoQueryModel.LottoQueryDetailModel detailModel = get(position);
			
			if (position == 0) {
				holder.setText(R.id.item_lotto_reward_name, detailModel.getPrize_name()).setTextColor(specialColor);
				holder.setText(R.id.item_lotto_reward_count, detailModel.getPrize_num()).setTextColor(specialColor);
				holder.setText(R.id.item_lotto_reward_money, detailModel.getPrize_amount()).setTextColor(specialColor);
				holder.setText(R.id.item_lotto_reward_condition, detailModel.getPrize_require()).setTextColor(specialColor);
			} else {
				holder.setText(R.id.item_lotto_reward_name, detailModel.getPrize_name()).setTextColor(normalColor);
				holder.setText(R.id.item_lotto_reward_count, detailModel.getPrize_num()).setTextColor(normalColor);
				holder.setText(R.id.item_lotto_reward_money, detailModel.getPrize_amount()).setTextColor(normalColor);
				holder.setText(R.id.item_lotto_reward_condition, detailModel.getPrize_require()).setTextColor(normalColor);
			}
		}
		
		@Override
		public void setDataList(List<LottoQueryModel.LottoQueryDetailModel> list, boolean isNotify) {
			if (null != list) {
				LottoQueryModel.LottoQueryDetailModel detailModel = new LottoQueryModel.LottoQueryDetailModel();
				detailModel.setPrize_name("奖项");
				detailModel.setPrize_num("中奖注数");
				detailModel.setPrize_amount("单注奖金(元)");
				detailModel.setPrize_require("中奖条件");
				list.add(0, detailModel);
			}
			super.setDataList(list, isNotify);
		}
	}
}
