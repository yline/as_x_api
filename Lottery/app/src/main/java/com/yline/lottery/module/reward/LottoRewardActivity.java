package com.yline.lottery.module.reward;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yline.base.BaseActivity;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoQueryModel;

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
		// initData();
	}
	
	private void initView() {
		initViewClick();
	}
	
	private void initViewClick() {
	
	}
	
	private void initData() {
		OkHttpManager.lottoQuery(mLottoId, mLottoNumber, new OnJsonCallback<LottoQueryModel>() {
			@Override
			public void onFailure(int code, String msg) {
				// todo refresh
			}
			
			@Override
			public void onResponse(LottoQueryModel lottoQueryModel) {
				// 界面展示数据
			}
		});
	}
}
