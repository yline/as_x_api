package com.yline.lottery.module.reward;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseActivity;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoQueryModel;
import com.yline.lottery.module.reward.view.LottoRewardHeaderView;
import com.yline.lottery.view.LoadingView;
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
	private static final String LOTTO_NUMBER = "lottery_number"; // 彩票期号
	
	/**
	 * @param id     彩票种类
	 * @param number 彩票期号
	 */
	public static void launch(Context context, String id, String number) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, LottoRewardActivity.class);
			intent.putExtra(LOTTO_ID, id);
			intent.putExtra(LOTTO_NUMBER, number);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private String mLottoId;
	private String mLottoNumber;
	
	private LoadingView mLoadingView;
	private LottoRewardRecyclerAdapter mRecyclerAdapter;
	private LottoRewardHeaderView mHeaderView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lotto_reward);
		
		Intent intent = getIntent();
		if (null != intent) {
			mLottoId = intent.getStringExtra(LOTTO_ID);
			mLottoNumber = intent.getStringExtra(LOTTO_NUMBER);
		}
		
		initView();
		initData();
	}
	
	private void initView() {
		mLoadingView = findViewById(R.id.lotto_reward_loading);
		
		mRecyclerAdapter = new LottoRewardRecyclerAdapter(this);
		RecyclerView recyclerView = findViewById(R.id.lotto_reward_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(mRecyclerAdapter);
		
		mHeaderView = new LottoRewardHeaderView(this);
		mRecyclerAdapter.addHeadView(mHeaderView);
		
		initViewClick();
	}
	
	private void initViewClick() {
		mLoadingView.setOnReloadClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initData();
			}
		});
	}
	
	private void initData() {
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
					
					mHeaderView.setData(lottoQueryModel.getLottery_name(), lottoQueryModel.getLottery_no(),
							lottoQueryModel.getLottery_date(), lottoQueryModel.getLottery_exdate(), lottoQueryModel.getLottery_res(),
							lottoQueryModel.getLottery_sale_amount(), lottoQueryModel.getLottery_pool_amount());
					mRecyclerAdapter.setDataList(lottoQueryModel.getLottery_prize(), true);
				} else {
					mLoadingView.loadFailed();
				}
			}
		});
	}
	
	private class LottoRewardRecyclerAdapter extends AbstractHeadFootRecyclerAdapter<LottoQueryModel.LottoQueryDetailModel> {
		private LottoRewardRecyclerAdapter(Context context) {
			super(context);
		}
		
		@Override
		public int getItemRes() {
			return R.layout.item_lotto_reward;
		}
		
		@Override
		public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
			LottoQueryModel.LottoQueryDetailModel detailModel = get(position);
			
			holder.setText(R.id.item_lotto_reward_name, detailModel.getPrize_name());
			holder.setText(R.id.item_lotto_reward_count, detailModel.getPrize_num());
			holder.setText(R.id.item_lotto_reward_money, detailModel.getPrize_amount());
			holder.setText(R.id.item_lotto_reward_condition, detailModel.getPrize_require());
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
