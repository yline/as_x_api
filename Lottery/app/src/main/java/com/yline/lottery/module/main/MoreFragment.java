package com.yline.lottery.module.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.bugly.BuglyConfig;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoBonusModel;
import com.yline.lottery.module.feedback.FeedbackActivity;
import com.yline.lottery.module.rule.LottoRuleActivity;
import com.yline.lottery.sp.SPManager;
import com.yline.test.BaseTestFragment;

public class MoreFragment extends BaseTestFragment {
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("展示规则", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoRuleActivity.launch(getActivity());
			}
		});
		
		addButton("中奖计算器", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String lotteryId = SPManager.getInstance().getHistoryLotteryId();
				if (TextUtils.isEmpty(lotteryId)) {
					return;
				}
				
				String lotteryNum = "01,11,02,09,14,22,25@05,03";
				OkHttpManager.lottoBonus(lotteryId, null, lotteryNum, new OnJsonCallback<LottoBonusModel>() {
					@Override
					public void onFailure(int code, String msg) {
					
					}
					
					@Override
					public void onResponse(LottoBonusModel lottoBonusModel) {
					
					}
				});
			}
		});
		
		addButton("升级", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SDKManager.toast("升级");
				BuglyConfig.checkUpgrade();
			}
		});
		
		addButton("帮助与反馈", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedbackActivity.launch(getActivity());
			}
		});
	}
}
